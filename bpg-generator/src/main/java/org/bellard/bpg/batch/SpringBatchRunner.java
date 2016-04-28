package org.bellard.bpg.batch;

import java.util.HashMap;
import java.util.Map;

import org.bellard.bpg.model.ProcessData;
import org.bellard.bpg.service.JSonService;
import org.bellard.bpg.service.ProcessDataService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SpringBatchRunner {

	@Autowired
	@Qualifier(BatchConstants.ASYNC_JOB_LAUNCHER)
	JobLauncher jobLauncher;

	@Autowired
	@Qualifier(BatchConstants.BPEG_CONVERTOR_JOB)
	private Job bpegCreateJob;

	@Autowired
	private JSonService jsonService;

	@Autowired
	ProcessDataService dataService;

	public JobExecution runBpgGeneratorJob(ProcessData data) throws Exception {

		dataService.saveOrUpdate(data);

		String processData = jsonService.processData2json(data);

		JobParameters jobParameters = createBpgGeneratorParameters(data, processData);

		JobExecution firstExecution = jobLauncher.run(bpegCreateJob, jobParameters);

		return firstExecution;
	}

	protected JobParameters createBpgGeneratorParameters(ProcessData data, String processData) {
		Map<String, JobParameter> map = new HashMap<String, JobParameter>();
		map.put(BatchConstants.PROCESS_DATA_UUID, new JobParameter(data.getId()));

		// TODO do something for processData values
		JobParameters jobParameters = new JobParameters(map);
		return jobParameters;
	}

}
