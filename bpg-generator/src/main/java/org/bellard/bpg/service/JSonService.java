package org.bellard.bpg.service;

import java.io.IOException;

import org.bellard.bpg.core.Processor;
import org.bellard.bpg.exception.TechnicalException;
import org.bellard.bpg.model.ExceptionData;
import org.bellard.bpg.model.ProcessData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JSonService {

	final static Logger logger = LoggerFactory.getLogger(JSonService.class);

	private ObjectMapper objectMapper = new ObjectMapper();

	<I, O> O executeAndManageException(Processor<I, O, IOException> processor, I input) {
		try {
			return processor.process(input);
		} catch (IOException e) {
			logger.info(ExceptionData.JSON_MAPPING_ERROR.toString(), e);
			throw new TechnicalException(ExceptionData.JSON_MAPPING_ERROR, e);
		}
	}

	public ProcessData json2processData(String json) {

		return executeAndManageException((String s) -> objectMapper.readValue(s, ProcessData.class), json);
	}

	public String processData2json(ProcessData data) {
		return executeAndManageException((ProcessData s) -> objectMapper.writeValueAsString(s), data);
	}

}
