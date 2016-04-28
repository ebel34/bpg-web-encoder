/**
 * 
 */
package org.bellard.bpg.web;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.validator.GenericValidator;
import org.bellard.bpg.batch.SpringBatchRunner;
import org.bellard.bpg.batch.job.BpgGeneratorJob;
import org.bellard.bpg.batch.step.NotifyFailureStep;
import org.bellard.bpg.exception.BusinessException;
import org.bellard.bpg.exception.TechnicalException;
import org.bellard.bpg.model.AppSession;
import org.bellard.bpg.model.ExceptionData;
import org.bellard.bpg.model.ProcessData;
import org.bellard.bpg.model.UploadedFile;
import org.bellard.bpg.model.UploadedFiles;
import org.bellard.bpg.service.AppSessionService;
import org.bellard.bpg.service.FileResourceService;
import org.bellard.bpg.service.InstantService;
import org.bellard.bpg.service.ProcessDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.code.kaptcha.Constants;

/**
 * @author ebellard
 *
 */
@Controller
public class BpgController {

	private static final Logger logger = LoggerFactory.getLogger(BpgController.class);

	@Value("${spring.batch.enabled}")
	@Autowired
	private boolean springBatchEnabled;

	@Autowired
	private ProcessDataService processDataService;

	@Autowired
	private AppSessionService appSessionService;

	@Autowired
	private FileResourceService fileResourceService;

	@Autowired
	private InstantService instantService;

	@Autowired
	private BpgGeneratorJob bpgGeneratorJob;

	@Autowired
	private NotifyFailureStep notifyFailureStep;

	@Autowired
	private SpringBatchRunner springBatchRunner;

	@RequestMapping(value = "/view/{sessionId}/{fileId}", method = RequestMethod.GET)
	public void viewBpgFile(@PathVariable("sessionId") String sessionId, @PathVariable("fileId") String fileId,
			HttpServletResponse response) {
		processDownload(sessionId, fileId, response, true);

	}

	protected void processDownload(String sessionId, String filename, HttpServletResponse response,
			boolean attachment) {
		logger.info("sessionId=" + sessionId + ",filename=" + filename);

		Resource generatedFile = fileResourceService.createBpg(filename);
		ProcessData data = appSessionService.getProcessData(sessionId, filename);

		if (generatedFile.exists()) {
			try {
				logger.info("download file...");
				if (data != null) {
					data.getSteps().add("downloadBpgFile");
				}
				response.setContentType("image/bpg");
				if (attachment) {
					if (data != null) {
						response.setHeader("Content-Disposition",
								"attachment; filename=" + data.getVisibleOutputFilename());
					}
				}
				// get your file as InputStream
				InputStream is = generatedFile.getInputStream();
				// copy it to response's OutputStream
				IOUtils.copy(is, response.getOutputStream());
				response.flushBuffer();
				logger.info("download file DONE.");

				// //////////////////////// NOTIFY
				if (data != null) {
					data.setLastOperation(instantService.now());
				}

			} catch (IOException e) {
				// //////////////////////// NOTIFY FAILURE
				logger.info(ExceptionData.RESOURCE_DOWNLOAD.toString(), e);
				if (data != null) {
					data.setLastOperation(instantService.now());
				}
				throw new TechnicalException(ExceptionData.RESOURCE_DOWNLOAD, data, e);
			}
		} else {
			// //////////////////////// NOTIFY FAILURE
			logger.info(ExceptionData.RESOURCE_NO_DOWNLOAD.toString());
			if (data != null) {
				data.setLastOperation(instantService.now());
			}
			throw new TechnicalException(ExceptionData.RESOURCE_DOWNLOAD, data);
		}
	}

	@RequestMapping(value = "/download/{sessionId}/{fileId}", method = RequestMethod.GET)
	public void downloadBpgFile(@PathVariable("sessionId") String sessionId, @PathVariable("fileId") String fileId,
			HttpServletResponse response) {
		processDownload(sessionId, fileId, response, true);

	}

	@RequestMapping(value = "/uploads", method = RequestMethod.POST)
	public @ResponseBody UploadedFiles uploadImageFiles(@RequestParam("captcha") String captcha,
			@RequestParam("hash") String sessionId, @RequestParam("files[]") MultipartFile[] files,
			@RequestParam("quantizer") int quantizer, @RequestParam("chroma") int chroma,
			@RequestParam("colorSpace") String colorSpace, @RequestParam("level") int level,
			@RequestParam("bitDepth") int bitDepth, HttpServletRequest request) {

		logger.info(ToStringBuilder.reflectionToString(request.getParameterMap()));
		logger.info("uploadImageFiles starts...");

		String sessionKaptcha = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY + sessionId);

		Instant captchaTime = (Instant) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_DATE + sessionId);

		UploadedFiles ufiles = new UploadedFiles();
		if (GenericValidator.isBlankOrNull(captcha) || !captcha.equals(sessionKaptcha) || captchaTime == null
				|| ChronoUnit.MINUTES.between(captchaTime, instantService.now()) > 3) {
			// //////////////////////// NOTIFY FAILURE
			// TODO create dump action
			// TODO create notify failure
			String msg = "File upload failed : the captcha decoding isn't correct or the captcha is older than 3 minutes.";
			logger.info(msg);
			UploadedFile ufile = new UploadedFile();
			ufile.setSuuid(sessionId);
			ufile.setError(msg);
			ufiles.getFiles().add(ufile);
		} else {
			if (files == null || files.length == 0) {
				UploadedFile ufile = new UploadedFile();
				ufile.setSuuid(sessionId);
				ufiles.getFiles().add(ufile);
				String msg = "File upload failed : No file uploaded.";
				logger.info(msg);
				ufile.setError(msg);
				ufiles.getFiles().add(ufile);
			} else {
				appSessionService.getSession(sessionId);

				for (int i = 0; i < files.length; i++) {
					MultipartFile file = files[i];
					UploadedFile ufile = new UploadedFile();
					ufiles.getFiles().add(ufile);

					if (file == null) {
						String msg = "File upload failed : the file is empty!";
						logger.info(msg);
						ufile.setError(msg);
					} else {

						ufile.setName(file.getOriginalFilename());

						if (file.isEmpty()) {
							// //////////////////////// NOTIFY FAILURE
							// TODO create dump action
							// TODO create notify failure
							String msg = "File upload failed : the file is empty!";
							logger.info(msg);
							ufile.setError(msg);

						} else if (!file.getOriginalFilename().toUpperCase().endsWith("JPG")
								&& !file.getOriginalFilename().toUpperCase().endsWith("JPEG")
								&& !file.getOriginalFilename().toUpperCase().endsWith("PNG")) {
							// TODO create dump action
							// TODO create notify failure
							String msg = "File upload failed :the file format isn't correct, it must be a valid jpg/png image.";
							logger.info(msg);
							ufile.setError(msg);

						} else {
							try {

								logger.info("get input file...");
								ProcessData pdata = processDataService.createProcessData(file.getOriginalFilename(),
										sessionId);
								pdata.setBpgQuantizer(quantizer);
								pdata.setBpgChroma(chroma);
								pdata.setBpgColorSpace(colorSpace);
								pdata.setBpgLevel(level);
								pdata.setBpgBitDepth(bitDepth);
								Resource destination = fileResourceService.createUpload(pdata.getId());
								byte[] bytes = file.getBytes();
								BufferedOutputStream stream = new BufferedOutputStream(
										new FileOutputStream(destination.getFile()));
								stream.write(bytes);
								stream.close();
								pdata.setLastOperation(instantService.now());

								logger.info("get input file DONE.");

								// //////////////////////// PROCESS THE FILE
								// HINT : switch between spring batch and non
								// spring batch process
								if (springBatchEnabled) {
									springBatchRunner.runBpgGeneratorJob(pdata);
								} else {
									bpgGeneratorJob.process(pdata);
								}
								
								// //////////////////////// REDIRECT
								logger.info("redirect...");

								// TODO redirect...
								// String turl = request.getContextPath() +
								// "/download/" + pdata.getId();
								//
								// String html = "<a href=\"" + turl + "\">" +
								// pdata.getVisibleOutputFilename() +
								// "</a>";

								ufile.setBpgname(pdata.getVisibleOutputFilename());

								ufile.setUrl("/download/" + pdata.getId());
								ufile.setSize((int) file.getSize());
								ufile.setUuid(pdata.getId());
								ufile.setSuuid(sessionId);
							} catch (BusinessException e) {
								String msg = "File upload failed : " + e.getData().getMessage();
								logger.info(msg, e);
								logger.info("notify failure...");

								// notify failure
								ufile.setError(msg);
								notifyFailureStep.notify(ufile);
								logger.info("notify failure DONE.");

							} catch (Exception e) {
								// //////////////////////// NOTIFY FAILURE
								String msg = "File upload failed : unknown cause.";
								logger.info(msg);
								logger.info("notify failure...");

								// notify failure
								ufile.setError(msg);
								notifyFailureStep.notify(ufile);
								logger.info("notify failure DONE.");
							}
						}
					}
				}
			}
		}
		logger.info("uploadImageFiles ends.");
		return ufiles;
	}

	@RequestMapping(value = "/encodedData/{sessionId}", method = RequestMethod.GET)
	public @ResponseBody UploadedFiles getEncodedData(@PathVariable("sessionId") String sessionId) {
		UploadedFiles files = new UploadedFiles();
		AppSession session = appSessionService.getSession(sessionId);
		Collection<ProcessData> datas = session.getProcessDatas();

		for (ProcessData data : datas) {
			if ((data.isEncoded() && !data.isClientAjaxNotified())
					|| !GenericValidator.isBlankOrNull(data.getError())) {
				UploadedFile ufile = new UploadedFile();
				ufile.setSuuid(sessionId);
				ufile.setUrl("/download/" + data.getId());
				ufile.setUuid(data.getId());
				ufile.setEncoded(data.isEncoded());
				if (data.isEncoded()) {
					ufile.setEncodingTime(data.getEncodingDuration().getSeconds() + " seconds");
				}
				ufile.setError(data.getError());
				ufile.setBpgSize(data.getBpgSize());
				files.getFiles().add(ufile);
				data.setClientAjaxNotified(true);
				appSessionService.updateProcessData(sessionId, data);
			}
		}
		return files;
	}

}
