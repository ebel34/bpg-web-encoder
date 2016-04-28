package org.bellard.bpg.batch.step;

import org.bellard.bpg.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public 	abstract class NotifyStep {

	final static Logger logger = LoggerFactory.getLogger(NotifyStep.class);

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	public void notify(UploadedFile ufile) {
		this.messagingTemplate.convertAndSend("/topic/updateData", ufile);
		logger.info("after notify...");

	}
}
