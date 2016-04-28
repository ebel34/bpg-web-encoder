package org.bellard.bpg.batch;

import org.bellard.bpg.exception.BusinessException;
import org.bellard.bpg.exception.TechnicalException;
import org.bellard.bpg.model.ExceptionData;
import org.bellard.bpg.model.ProcessData;
import org.bellard.bpg.service.ProcessDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractTasklet implements Tasklet {

	final static Logger logger = LoggerFactory.getLogger(AbstractTasklet.class);

	@Autowired
	private ProcessDataService dataService;

	
	
	public AbstractTasklet(ProcessDataService dataService) {
		super();
		this.dataService = dataService;
	}

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

	protected ProcessData getAndCheckProcessData(ChunkContext chunkContext) throws BusinessException {
		String dataId = getAndCheckDataId(chunkContext);
		ProcessData data = dataService.get(dataId);
		if (data == null) {
			throw new BusinessException(ExceptionData.NO_PROCESS_DATA);
		}
		return data;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		logger.info("tasklet begins...");
		ProcessData data = getAndCheckProcessData(chunkContext);

		// TODO to delete because of spring batch 
		data.getSteps().add(chunkContext.getStepContext().getStepName());

		process(data);

		dataService.saveOrUpdate(data);
		
		contribution.incrementWriteCount(1);
		logger.info("tasklet ends...");
		return RepeatStatus.FINISHED;

	}

	public abstract void process(ProcessData data) throws BusinessException;

}
