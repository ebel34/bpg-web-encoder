package org.bellard.bpg.service;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.bellard.bpg.exception.TechnicalException;
import org.bellard.bpg.model.ExceptionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class FileResourceService {

	final static Logger logger = LoggerFactory.getLogger(FileResourceService.class);

	@Value("${upload.dir}")
	private Resource uploadDir;

	@Value("${output.dir}")
	private Resource outputDir;

	@PostConstruct
	protected void init() {

		if (uploadDir == null || outputDir == null) {
			String msg = "the input and output dir must be set! Check application.properties, property upload.dir and output.dir.";
			logger.error(msg);
			throw new IllegalStateException(msg);
		}
	}

	protected Resource createRelative(Resource resource, String filename) {
		try {
			return resource.createRelative(filename);
		} catch (IOException e) {
			logger.warn("" + ExceptionData.RESOURCE_CREATE);
			throw new TechnicalException(ExceptionData.RESOURCE_CREATE, e);
		}
	}

	public Resource createBpg(String filename) {
		String file = filename;
		if (!file.toUpperCase().endsWith(".BPG")) {
			file = file + ".bpg";
		}
		return createRelative(outputDir, file);
	}

	public long getBpgFileSize(String filename) {
		long l;
		try {
			l = createBpg(filename).getFile().length();
		} catch (IOException e) {
			logger.warn("Problem when retrieving the file size");
			l = -1;
		}
		return l;
	}

	public Resource createUpload(String filename) {
		return createRelative(uploadDir, filename);
	}

	public void deleteUpload(String id) {
		delete(createUpload(id));
	}

	public void deleteOutput(String id) {
		delete(createBpg(id));
	}

	protected void delete(Resource resource) {
		try {
			if (resource.exists()) {
				resource.getFile().delete();
			}
		} catch (IOException e) {
			logger.warn("" + ExceptionData.RESOURCE_DELETE);
			throw new TechnicalException(ExceptionData.RESOURCE_DELETE, e);
		}

	}

}
