package org.bellard.bpg.web;

import java.util.Map;

import org.bellard.bpg.service.ApplicationVersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VersionController {

	final static Logger logger = LoggerFactory.getLogger(VersionController.class);
	@Autowired
	private ApplicationVersionService applicationVersionService;

	@RequestMapping("/version")
	public String redirectVersion(Map<String, String> model) {
		logger.info("redirectVersion...");
		model.put("appName", applicationVersionService.getAppName());
		model.put("appVersion", applicationVersionService.getAppVersion());
		model.put("buildTimestamp", applicationVersionService.getBuildTimestamp());
		return "fversion";
	}

}
