package org.bellard.bpg.batch.step;

import java.util.concurrent.Future;

import org.bellard.bpg.action.Actions;
import org.bellard.bpg.batch.AbstractProcessDataManager;
import org.bellard.bpg.batch.StepState;
import org.bellard.bpg.exception.BusinessException;
import org.bellard.bpg.model.ProcessData;
import org.bellard.bpg.service.FileResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

public class BpgSystemCommandStep extends AbstractProcessDataManager {

	final static Logger logger = LoggerFactory.getLogger(BpgSystemCommandStep.class);

	@Value("${output.dir}")
	private org.springframework.core.io.Resource outputDir;

	@Autowired
	FileResourceService fileResourceService;

	@Autowired
	Actions actions;

	@Async("SystemCommandExecutor")
	public Future<StepState> process(ProcessData pdata) throws BusinessException {

		return execute(pdata);

	}

	public Future<StepState> execute(ProcessData pdata) throws BusinessException {
		logger.info("run system command...");


//		ProcessBuilder processBuilder = actions.createBpegEncProcessBuilder().apply(pdata.getId());
		ProcessBuilder processBuilder = actions.createAdvancedBpegEncProcessBuilder().apply(pdata);

		if (logger.isDebugEnabled()) {
			logger.debug("command=" + processBuilder.command());
		}

		Future<StepState> future = null;

		try {
			future = actions.createExecuteBpegEncProcessBuilder().process(processBuilder);
		} catch (BusinessException e) {
			update(pdata);
			throw e;
		}
		update(pdata);
		return future;
	}
}
