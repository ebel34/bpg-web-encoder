package org.bellard.bpg.batch;

import java.util.concurrent.Executor;

import org.bellard.bpg.AppAsyncUncaughtExceptionHandler;
import org.bellard.bpg.batch.job.BpgGeneratorJob;
import org.bellard.bpg.batch.step.BpgSystemCommandStep;
import org.bellard.bpg.batch.step.RemoveDataStep;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
@EnableAsync
public class BatchConfig implements AsyncConfigurer {

	@Value("${aync.thread.pool.size}")
	private int asyncThreadPoolSize = 1;
	@Value("${aync.thread.pool.queue}")
	private int asyncThreadPoolQueue = 100;

	@Value("${encoder.thread.pool.size}")
	private int encoderThreadPoolSize = 1;
	@Value("${encoder.thread.pool.queue}")
	private int encoderThreadPoolQueue = 100;

	@Autowired
	private AppAsyncUncaughtExceptionHandler appAsyncUncaughtExceptionHandler;

	@Bean
	public BpgGeneratorJob getBpgGeneratorJob() {
		return new BpgGeneratorJob();
	}

	@Bean
	public RemoveDataStep getRemoveDataStep() {
		return new RemoveDataStep();
	}

	@Bean
	public BpgSystemCommandStep getBpgSystemCommandStep() {
		return new BpgSystemCommandStep();
	}

	@Bean(name = "SystemCommandExecutor")
	public Executor getSystemCommandExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(encoderThreadPoolSize);
		executor.setMaxPoolSize(encoderThreadPoolSize);
		executor.setQueueCapacity(encoderThreadPoolQueue);
		executor.setThreadNamePrefix("BpgEnc-");
		executor.initialize();
		return executor;
	}

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(asyncThreadPoolSize);
		executor.setMaxPoolSize(asyncThreadPoolSize);
		executor.setQueueCapacity(asyncThreadPoolQueue);
		executor.setThreadNamePrefix("JobStepExecutor-");
		executor.initialize();
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return appAsyncUncaughtExceptionHandler;
	}

	@Bean(name="taskScheduler")
	public TaskScheduler getTaskScheduler() {
		ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
		executor.setPoolSize(asyncThreadPoolSize);
		executor.setThreadNamePrefix("ScheduleJobStepExecutor-");
		executor.initialize();
		return executor;
		
	}
	
}
