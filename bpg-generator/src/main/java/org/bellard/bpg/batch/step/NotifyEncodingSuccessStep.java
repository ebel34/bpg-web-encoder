package org.bellard.bpg.batch.step;

import org.apache.commons.validator.GenericValidator;
import org.bellard.bpg.model.ProcessData;
import org.bellard.bpg.model.UploadedFile;
import org.bellard.bpg.service.AppSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotifyEncodingSuccessStep extends NotifyStep {

	@Autowired
	private AppSessionService appSessionService;

	public void notify(ProcessData data) {
		logger.info("notify...");
		if ((data.isEncoded() && !data.isClientAjaxNotified()) || !GenericValidator.isBlankOrNull(data.getError())) {
			UploadedFile ufile = new UploadedFile();
			ufile.setSuuid(data.getSessionId());
			ufile.setUrl("/download/" + data.getId());
			ufile.setUuid(data.getId());
			ufile.setEncoded(data.isEncoded());
			if (data.isEncoded()) {
				ufile.setEncodingTime(data.getEncodingDuration().getSeconds() + " seconds");
			}
			ufile.setError(data.getError());
			ufile.setBpgSize(data.getBpgSize());
			data.setClientAjaxNotified(true);
			appSessionService.updateProcessData(data.getSessionId(), data);
			notify(ufile);
		}
	}
}
