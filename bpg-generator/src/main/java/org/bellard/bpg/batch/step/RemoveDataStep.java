package org.bellard.bpg.batch.step;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.annotation.Resource;

import org.bellard.bpg.action.Actions;
import org.bellard.bpg.batch.AbstractProcessDataManager;
import org.bellard.bpg.model.ProcessData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;

public class RemoveDataStep extends AbstractProcessDataManager {

	final static Logger logger = LoggerFactory.getLogger(RemoveDataStep.class);

	@Value("${data.remove.delay}")
	private int dataRemoveDelay = 15;

	@Value("${data.remove.retry}")
	private int dataRemoveRetry = 1;

	@Autowired
	private Actions actions;

	@Resource(name="taskScheduler")
	private TaskScheduler scheduler;

	protected void deleteData(ProcessData data) {

		try {
			actions.removeDataFunction().apply(data.getId());
			update(data);

		} catch (Exception e) {
			logger.warn("Problem when deleting data : data.id" + data.getId());
			logger.warn("Reschedule deleting 1 time : data.id" + data.getId());
			Instant t = Instant.now();
			t = t.plus(dataRemoveRetry, ChronoUnit.MINUTES);
			scheduler.schedule(() -> actions.removeDataFunction().apply(data.getId()), Date.from(t));
			update(data);
		}
	}

	public void process(ProcessData data) {

		Instant t = Instant.now();
		t = t.plus(dataRemoveDelay, ChronoUnit.MINUTES);
		scheduler.schedule(() -> deleteData(data), Date.from(t));

	}
}
