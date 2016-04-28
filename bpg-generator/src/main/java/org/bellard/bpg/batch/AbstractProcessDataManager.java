package org.bellard.bpg.batch;

import org.bellard.bpg.exception.BusinessException;
import org.bellard.bpg.model.ProcessData;
import org.bellard.bpg.service.AppSessionService;
import org.bellard.bpg.service.InstantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractProcessDataManager {

	final static Logger logger = LoggerFactory.getLogger(AbstractProcessDataManager.class);

	@Autowired
	private InstantService instantService;
	@Autowired
	private AppSessionService appSessionService;

	protected ProcessData update(ProcessData data) {
		data.getSteps().add(this.getClass().getSimpleName());
		data.setLastOperation(instantService.now());
		if (logger.isDebugEnabled()) {
			logger.debug("data=" + data);
		}
		try {
			appSessionService.putProcessData(data.getSessionId(), data);
		} catch (BusinessException e) {
			// eat exception
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}
		}
		return data;
	}

}
