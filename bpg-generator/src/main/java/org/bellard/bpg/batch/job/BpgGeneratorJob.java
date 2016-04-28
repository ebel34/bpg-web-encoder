package org.bellard.bpg.batch.job;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.bellard.bpg.batch.AbstractProcessDataManager;
import org.bellard.bpg.batch.StepState;
import org.bellard.bpg.batch.step.BpgSystemCommandStep;
import org.bellard.bpg.batch.step.ImageCheckFilterStep;
import org.bellard.bpg.batch.step.NotifyEncodingSuccessStep;
import org.bellard.bpg.batch.step.RemoveDataStep;
import org.bellard.bpg.exception.BusinessException;
import org.bellard.bpg.exception.TechnicalException;
import org.bellard.bpg.model.ExceptionData;
import org.bellard.bpg.model.ProcessData;
import org.bellard.bpg.service.FileResourceService;
import org.bellard.bpg.service.HashService;
import org.bellard.bpg.service.InstantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

public class BpgGeneratorJob extends AbstractProcessDataManager {

	final static Logger logger = LoggerFactory.getLogger(BpgGeneratorJob.class);

	@Value("${output.dir}")
	private org.springframework.core.io.Resource outputDir;

	@Autowired
	private InstantService instantService;

	@Autowired
	private BpgSystemCommandStep bpgConvertor;

	@Autowired
	private ImageCheckFilterStep imageCheckFilterStep;

	@Autowired
	private NotifyEncodingSuccessStep encodingSuccessStep;

	@Autowired
	private RemoveDataStep removeDataStep;

	@Autowired
	protected FileResourceService fileResourceService;

	@Async
	public void process(ProcessData data) throws BusinessException {
		try {
			// //////////////////////// CHECK
			// create check action
			logger.info("check data...");

			update(data);

			if (imageCheckFilterStep.accept(data)) {

				logger.info("check data DONE.");
				// //////////////////////// CONVERT
				logger.info("convert data...");

				Future<StepState> state = bpgConvertor.process(data);

				Instant start = instantService.now();
				if (StepState.SUCCESS.equals(state.get())) {
					data.setEncoded(true);
					data.setBpgSize(fileResourceService.getBpgFileSize(data.getId()));
					Instant end= Instant.now();
					data.setEncodingDuration(Duration.between(start, end));
					logger.info("convert DONE.");
					
					// //////////////////////// SCHEDULE REMOVE DATA
					removeDataStep.process(data);

					// //////////////////////// NOTIFY
					logger.info("notify success...");

					encodingSuccessStep.notify(data);
				} else {
					logger.info("notify failure...");

				}
				logger.info("notify DONE.");
			}
			update(data);
		} catch (BusinessException e) {
			logger.info(e.getData().toString());
			e.setProcessData(data);
			throw e;
		} catch (ExecutionException | InterruptedException e) {
			logger.info(e.getMessage(), e);
			throw new TechnicalException(ExceptionData.THREAD_ENCODING_ERROR, data, e);
		}
	}

}
