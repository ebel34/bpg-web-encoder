package org.bellard.bpg.service;

import java.util.HashMap;
import java.util.Map;

import org.bellard.bpg.exception.BusinessException;
import org.bellard.bpg.model.AppSession;
import org.bellard.bpg.model.ExceptionData;
import org.bellard.bpg.model.ProcessData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppSessionService {

	final static Logger logger = LoggerFactory.getLogger(AppSessionService.class);

	@Value("${session.max.upload}")
	private Integer sessionMaxUpload = 10;

	private Map<String, AppSession> map = new HashMap<String, AppSession>();

	public AppSession getSession(String sessionId) {
		AppSession appSession = map.get(sessionId);
		if (appSession == null) {
			appSession = new AppSession(sessionId, sessionMaxUpload);
			map.put(sessionId, appSession);
		}
		return appSession;
	}

	public AppSession putProcessData(String sessionId, ProcessData data) throws BusinessException {
		AppSession appSession = map.get(sessionId);
		if (appSession != null) {
			if (appSession.putProcessData(data.getId(), data)) {
				return appSession;
			}
			throw new BusinessException(ExceptionData.MAX_FILE_PER_SESSION_EXCEED, data);

		}
		throw new BusinessException(ExceptionData.NO_SESSION, data);
	}

	public boolean updateProcessData(String sessionId, ProcessData data) {
		AppSession appSession = map.get(sessionId);
		if (appSession != null) {
			return appSession.putProcessData(data.getId(), data);
		}
		return false;
	}

	public ProcessData getProcessData(String sessionId, String dataId) {
		AppSession appSession = map.get(sessionId);
		if (appSession != null) {
			return appSession.getProcessData(dataId);
		}
		return null;
	}

}
