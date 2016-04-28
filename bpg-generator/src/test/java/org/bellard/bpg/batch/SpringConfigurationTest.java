package org.bellard.bpg.batch;

import org.bellard.bpg.JUnitTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(JUnitTestConfiguration.class)
public class SpringConfigurationTest {

	
	@Test
	public void test() {
		System.out.println("OK!");
		
	}
	
	
}
