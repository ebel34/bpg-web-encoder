package org.bellard.bpg.web;

import org.bellard.bpg.model.AppData;
import org.bellard.bpg.service.ApplicationVersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionRestController {

	final static Logger logger = LoggerFactory.getLogger(VersionRestController.class);

	@Autowired
	private ApplicationVersionService applicationVersionService;

	@RequestMapping("/version.json")
	public AppData redirectVersion() {
		logger.info("redirectVersion...");
		return applicationVersionService.getAppData();
	}

}
