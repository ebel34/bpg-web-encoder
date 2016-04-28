package org.bellard.bpg.web;

import java.util.Map;

import org.bellard.bpg.service.HashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@PropertySource("classpath:version.properties")
@Configuration
public class RedirectController {

	private static final Logger logger = LoggerFactory.getLogger(RedirectController.class);

	@Autowired
	private HashService hashService;


	@RequestMapping("/display/{suuid}/{uuid}")
	public String redirectDisplay(@PathVariable("suuid") String suuid, @PathVariable("uuid") String uuid,
			Map<String, String> model) {
		logger.info("redirectDisplay:suuid=" + suuid + ",uuid=" + uuid);
		model.put("hash", suuid + "/" + uuid);
		return "fdisplay";
	}

	@RequestMapping("/show")
	public String redirectShow(Map<String, String> model) {
		logger.info("redirectVersion : /show...");
		return "fdrop";
	}

	@RequestMapping("/")
	public String redirectJQ(Map<String, String> model) {
		logger.info("redirectJQ : /...");
		model.put("hash", hashService.createHashCode());
		return "fupload";
	}

	@RequestMapping("/support.html")
	public String redirectSupport(Map<String, String> model) {
		logger.info("redirectJQ : support.html...");
		model.put("hash", hashService.createHashCode());
		return "support";
	}

	@RequestMapping("/hello")
	public @ResponseBody String hello() {
		return "Hello World!";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public @ResponseBody String provideUploadInfo() {
		return "You can upload a file by posting to this same URL.";
	}

	@RequestMapping(value = "/uploads", method = RequestMethod.GET)
	public @ResponseBody String provideUploadsInfo() {
		return "You can upload files by posting to this same URL.";
	}

	@MessageMapping("/hello")
	public void greeting() throws Exception {
		logger.info("greeting");
	}

}
