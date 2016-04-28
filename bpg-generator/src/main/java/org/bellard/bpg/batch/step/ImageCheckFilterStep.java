package org.bellard.bpg.batch.step;

import org.bellard.bpg.action.Actions;
import org.bellard.bpg.batch.AbstractProcessDataManager;
import org.bellard.bpg.model.ProcessData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageCheckFilterStep extends AbstractProcessDataManager {

	@Autowired
	Actions actions;

	public boolean accept(ProcessData data) {

		boolean accept = actions.checkInputImagePredicate().test(data.getId());
		update(data);
		return accept;
	}

}
