package org.bellard.bpg.batch;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.commons.validator.GenericValidator;
import org.bellard.bpg.action.Actions;
import org.bellard.bpg.exception.BusinessException;
import org.bellard.bpg.exception.TechnicalException;
import org.bellard.bpg.model.ExceptionData;
import org.bellard.bpg.model.ProcessData;
import org.bellard.bpg.model.UploadedFile;
import org.bellard.bpg.service.FileResourceService;
import org.bellard.bpg.service.InstantService;
import org.bellard.bpg.service.ProcessDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

	private static final Logger logger = LoggerFactory.getLogger(SpringBatchConfig.class);

	@Value("${data.remove.delay}")
	private int dataRemoveDelay = 15;

	@Value("${data.remove.retry}")
	private int dataRemoveRetry = 1;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Resource(name = "taskScheduler")
	private TaskScheduler scheduler;

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Autowired
	Actions actions;

	@Autowired
	protected FileResourceService fileResourceService;

	@Autowired
	private InstantService instantService;

	@Autowired
	private ProcessDataService appSessionService;

	protected String getAndCheckDataId(ChunkContext chunkContext) {
		String dataId = getDataId(chunkContext);
		if (dataId == null || "".equals(dataId)) {
			throw new TechnicalException(ExceptionData.SPRING_BATCH_ISE);
		}
		return dataId;
	}

	protected String getDataId(ChunkContext chunkContext) {
		String dataId = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters()
				.getString(BatchConstants.PROCESS_DATA_UUID);
		return dataId;
	}

	@Bean(name = BatchConstants.BPEG_CONVERTOR_JOB)
	public Job job(@Qualifier("step1") Step step1, @Qualifier("step2") Step step2, @Qualifier("step31") Step step31,
			@Qualifier("step32") Step step32, @Qualifier("step4") Step step4) {

		logger.info("run job : " + BatchConstants.BPEG_CONVERTOR_JOB);
		Flow flow1 = new FlowBuilder<Flow>("flow1").from(step31()).end();
		Flow flow2 = new FlowBuilder<Flow>("flow2").from(step32()).end();

		return jobs.get(BatchConstants.BPEG_CONVERTOR_JOB).incrementer(new RunIdIncrementer()).start(step1).next(step2)
				.next(step32).next(step4).build();

		// return jobs.get(BatchConstants.BPEG_CONVERTOR_JOB).incrementer(new
		// RunIdIncrementer()).start(step1).next(step2)
		// .split(new SimpleAsyncTaskExecutor()).add(flow1,
		// flow2).next(step4).end().build();

		// return
		// jobs.get(BatchConstants.BPEG_CONVERTOR_JOB).start(step1).next(step2).build();
	}

	@Bean
	protected Step step1() {
		return steps.get("step1").tasklet(new AbstractTasklet(appSessionService) {
			@Override
			public void process(ProcessData data) throws BusinessException {
				if (!actions.checkInputImagePredicate().test(data.getOriginalFilename())) {
					throw new BusinessException(ExceptionData.INPUT_IMAGE_CHECK_ERROR, data);
				}
			}
		}).build();
	}

	@Bean
	protected Step step2() {
		return steps.get("step2").tasklet(new AbstractTasklet(appSessionService) {
			@Override
			public void process(ProcessData data) throws BusinessException {

				ProcessBuilder processBuilder = actions.createAdvancedBpegEncProcessBuilder().apply(data);

				if (logger.isDebugEnabled()) {
					logger.debug("command=" + processBuilder.command());
				}

				Future<StepState> state = actions.createExecuteBpegEncProcessBuilder().process(processBuilder);

				Instant start = instantService.now();
				try {
					if (StepState.SUCCESS.equals(state.get())) {
						data.setEncoded(true);
						data.setBpgSize(fileResourceService.getBpgFileSize(data.getId()));
						Instant end = Instant.now();
						data.setEncodingDuration(Duration.between(start, end));
						logger.info("convert DONE.");
					}
				} catch (InterruptedException | ExecutionException e) {
					throw new BusinessException(ExceptionData.BPGENC_ERROR, data, e);
				}

			}
		}).build();
	}

	@Bean
	public Step step31() {
		return steps.get("step31").tasklet(new AbstractTasklet(appSessionService) {
			public void process(ProcessData data) throws BusinessException {

				// TODO clear the data
				Instant t = Instant.now();
				t = t.plus(dataRemoveDelay, ChronoUnit.MINUTES);
				scheduler.schedule(() -> deleteData(data), Date.from(t));

			}
		}).build();
	}

	protected void deleteData(ProcessData data) {

		try {
			actions.removeDataFunction().apply(data.getId());

		} catch (Exception e) {
			logger.warn("Problem when deleting data : data.id" + data.getId());
			logger.warn("Reschedule deleting 1 time : data.id" + data.getId());
			Instant t = Instant.now();
			t = t.plus(dataRemoveRetry, ChronoUnit.MINUTES);
			scheduler.schedule(() -> actions.removeDataFunction().apply(data.getId()), Date.from(t));
		}
	}

	@Bean
	public Step step32() {
		return steps.get("step32").tasklet(new AbstractTasklet(appSessionService) {
			@Override
			public void process(ProcessData data) throws BusinessException {
				logger.info("step3.2: notify encoding success...");
				if ((data.isEncoded() && !data.isClientAjaxNotified())
						|| !GenericValidator.isBlankOrNull(data.getError())) {
					UploadedFile ufile = new UploadedFile();
					ufile.setSuuid(data.getSessionId());
					ufile.setUrl("/download/" + data.getId());
					ufile.setUuid(data.getId());
					ufile.setEncoded(data.isEncoded());
					if (data.isEncoded()) {
						ufile.setEncodingTime(data.getEncodingDuration().getSeconds() + " seconds");
					}
					ufile.setError(data.getError());
					ufile.setBpgSize(data.getBpgSize());
					data.setClientAjaxNotified(true);
					messagingTemplate.convertAndSend("/topic/updateData", ufile);
					logger.info("step3.2: notify done.");
				}
			}
		}).build();
	}

	@Bean
	public Step step4() {
		return steps.get("step4").tasklet(new AbstractTasklet(appSessionService) {
			public void process(ProcessData data) throws BusinessException {
			}
		}).build();
	}

	@Bean
	public Job bpegCreateJob() {
		Flow flow1 = new FlowBuilder<Flow>("flow1").from(step31()).end();
		Flow flow2 = new FlowBuilder<Flow>("flow2").from(step32()).end();

		return jobs.get("bpeg-convertor-job").incrementer(new RunIdIncrementer()).start(step1()).next(step2())
				.split(new SimpleAsyncTaskExecutor()).add(flow1, flow2).next(step4()).end().build();
	}

	@Bean(name = BatchConstants.ASYNC_JOB_LAUNCHER)
	public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {

		SimpleJobLauncher jl = new SimpleJobLauncher();
		jl.setJobRepository(jobRepository);
		jl.setTaskExecutor(new SimpleAsyncTaskExecutor());
		jl.afterPropertiesSet();
		return jl;

	}

}
