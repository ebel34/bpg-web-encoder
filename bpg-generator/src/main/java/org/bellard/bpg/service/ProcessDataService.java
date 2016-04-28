package org.bellard.bpg.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.bellard.bpg.exception.BusinessException;
import org.bellard.bpg.model.ProcessData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessDataService {

	@Autowired
	private InstantService instantService;
	@Autowired
	private AppSessionService appSessionService;
	@Autowired
	HashService hashService;

	private Map<String,ProcessData> database = new HashMap<>();
	
	public ProcessData createProcessData(String originalFilename, String sessionId) throws BusinessException {
		String uuid = hashService.createHashCode();
		ProcessData data = new ProcessData();
		data.setCreated(instantService.now());
		data.setId(uuid);
		appSessionService.putProcessData(sessionId, data);
		data.setOriginalFilename(originalFilename);
		data.setSessionId(sessionId);
		String visible = FilenameUtils.getBaseName(originalFilename) + ".bpg";
		data.setVisibleOutputFilename(visible);
		data.getSteps().add(getClass().getSimpleName() + ".handleFileUpload");
		data.setLastOperation(instantService.now());
		return data;
	}

	public ProcessData saveOrUpdate(ProcessData data) {
		data.setLastOperation(instantService.now());

		database.put(data.getId(), data);
		return data;
	}
	
	public ProcessData get(String uuid) {
		return database.get(uuid);
	}

}
