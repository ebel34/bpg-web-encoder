/**
 * 
 */
package org.bellard.bpg.batch;

import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bellard.bpg.JUnitTestConfiguration;
import org.bellard.bpg.model.ProcessData;
import org.bellard.bpg.service.AppSessionService;
import org.bellard.bpg.service.ProcessDataService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ebellard
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(JUnitTestConfiguration.class)
public class SpringBatchRunnerTest {

	private static final Logger logger = LoggerFactory.getLogger(SpringBatchRunnerTest.class);

	@Autowired
	private SpringBatchRunner springBatchRunner;
	@Autowired
	ProcessDataService dataService;
	@Autowired
	private AppSessionService appSessionService;
	@Autowired
	private JobExplorer jobExplorer;

	@Test
	public void testBatch() throws Exception {
		
		List<JobInstance> instances = jobExplorer.findJobInstancesByJobName(BatchConstants.BPEG_CONVERTOR_JOB, 0, 100);
		logger.info("job.instances="+instances.size());
		JobExecution execution = null;

		try {
			FileUtils.copyFileToDirectory(new ClassPathResource("images/example.jpg").getFile(),
					new FileSystemResource("/usr/local/exploitation/bpg-generator/data/upload/").getFile());

			String sessionId = "test";
			appSessionService.getSession(sessionId);
			ProcessData pdata = dataService.createProcessData("example.jpg", sessionId);
			dataService.saveOrUpdate(pdata);
			execution = springBatchRunner.runBpgGeneratorJob(pdata);
		} catch (Exception e) {
			logger.error("pb when launching batch : " + e, e);
			Assert.fail();
		}
		logger.info("fin de batch");
		logger.info(execution.getExecutionContext().toString());
		logger.info(execution.getStatus().toString());
		logger.info(execution.getEndTime().toString());
		logger.info(execution.getAllFailureExceptions().toString());
		instances = jobExplorer.findJobInstancesByJobName(BatchConstants.BPEG_CONVERTOR_JOB, 0, 100);
		logger.info("job.instances="+instances.size());

		logger.info("" + instances);
	}

}
