package org.bellard.bpg.action;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

import org.bellard.bpg.EncoderCommandBuilder;
import org.bellard.bpg.batch.StepState;
import org.bellard.bpg.core.BusinessProcessor;
import org.bellard.bpg.exception.BusinessException;
import org.bellard.bpg.exception.TechnicalException;
import org.bellard.bpg.model.ExceptionData;
import org.bellard.bpg.model.ProcessData;
import org.bellard.bpg.service.FileResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class Actions {

	public static final int ENCODING_TIMEOUT_DEFAULT = 3;

	final static Logger logger = LoggerFactory.getLogger(Actions.class);

	@Autowired
	protected FileResourceService fileResourceService;

	@Value("${data.remove}")
	private boolean dataRemove = true;

	@Value("${encoder.timeout}")
	private int encoderTimeout = ENCODING_TIMEOUT_DEFAULT;

	@Value("${bpeg.encoder.command.path}")
	private String bpgEncoderCommandPath;

	public Predicate<String> checkInputImagePredicate() {
		return (String filename) -> {
			Resource r = fileResourceService.createUpload(filename);

			boolean check = false;

			logger.info("file=" + r.toString());
			if (r.exists()) {
				check = true;
			}

			return check;
		};
	}

	public Function<ProcessData, ProcessBuilder> createAdvancedBpegEncProcessBuilder() {

		return (ProcessData procesData) -> {
			logger.info("Create processBuilder...");
			ProcessBuilder processBuilder = null;
			try {
				String outputFile = fileResourceService.createBpg(procesData.getId()).getFile().getAbsolutePath();
				String inputFile = fileResourceService.createUpload(procesData.getId()).getFile().getAbsolutePath();
				EncoderCommandBuilder builder = new EncoderCommandBuilder(bpgEncoderCommandPath, inputFile, outputFile);
				builder.setQuantizer(procesData.getBpgQuantizer()).setColorSpace(procesData.getBpgColorSpace())
						.setBitDepth(procesData.getBpgBitDepth()).setCompression(procesData.getBpgLevel())
						.setChroma(procesData.getBpgChroma());
				processBuilder = new ProcessBuilder(builder.build());
			} catch (IOException e) {
				logger.info(ExceptionData.RESOURCE_CREATE.toString(), e);
				throw new TechnicalException(ExceptionData.RESOURCE_CREATE, e);
			}
			logger.info("Create processBuilder DONE.");
			return processBuilder;
		};
	}

	public Function<String, ProcessBuilder> createBpegEncProcessBuilder() {

		return (String filename) -> {
			logger.info("Create processBuilder...");
			ProcessBuilder processBuilder = null;
			try {
				String outputFile = fileResourceService.createBpg(filename).getFile().getAbsolutePath();
				String inputFile = fileResourceService.createUpload(filename).getFile().getAbsolutePath();
				EncoderCommandBuilder builder = new EncoderCommandBuilder(bpgEncoderCommandPath, inputFile, outputFile);
				processBuilder = new ProcessBuilder(builder.build());
			} catch (IOException e) {
				logger.info(ExceptionData.RESOURCE_CREATE.toString(), e);
				throw new TechnicalException(ExceptionData.RESOURCE_CREATE, e);
			}
			logger.info("Create processBuilder DONE.");
			return processBuilder;
		};
	}

	public BusinessProcessor<ProcessBuilder, Future<StepState>> createExecuteBpegEncProcessBuilder() {

		return (ProcessBuilder processBuilder) -> {
			logger.info("Execute processBuilder...");
			boolean ended = false;
			int exitVal = -1;
			StopWatch sw = new StopWatch("bpgenc");
			sw.start();
			try {

				Process process = processBuilder.start();
				ended = process.waitFor(encoderTimeout, TimeUnit.MINUTES);
				logger.info("run system command DONE.");
				if (ended) {
					exitVal = process.exitValue();
				}
			} catch (IOException | InterruptedException e) {
				sw.stop();
				logger.info(sw.toString());
				logger.warn(ExceptionData.BPGENC_ERROR.toString(), e);
				throw new BusinessException(ExceptionData.BPGENC_ERROR, e);
			}

			if (!ended) {
				sw.stop();
				logger.info(sw.toString());
				logger.warn(ExceptionData.BPGENC_ERROR.toString());
				throw new BusinessException(ExceptionData.BPGENC_ERROR);
			} else {
				logger.info("bpgenc:exit=" + exitVal + "(-1 means it still runs...)");
			}
			sw.stop();
			logger.info(sw.toString());
			logger.info("Execute processBuilder DONE.");
			return new AsyncResult<StepState>(StepState.SUCCESS);

		};
	}

	public Function<String, Void> removeDataFunction() {
		return (String filename) -> {
			logger.info("remove data...");

			if (dataRemove) {
				try {
					fileResourceService.deleteUpload(filename);
					fileResourceService.deleteOutput(filename);
				} catch (Exception e) {
				}
			}
			logger.info("remove data DONE.");
			return null;
		};
	}

	public void setFileResourceService(FileResourceService fileResourceService) {
		this.fileResourceService = fileResourceService;
	}

}
