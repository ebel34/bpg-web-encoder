package org.bellard.bpg;

import java.lang.reflect.Method;

import org.bellard.bpg.batch.AbstractProcessDataManager;
import org.bellard.bpg.exception.ExceptionDataHolder;
import org.bellard.bpg.exception.ProcessDataHolder;
import org.bellard.bpg.model.ProcessData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

@Component
public class AppAsyncUncaughtExceptionHandler extends AbstractProcessDataManager implements
		AsyncUncaughtExceptionHandler {

	final static Logger logger = LoggerFactory.getLogger(AppAsyncUncaughtExceptionHandler.class);

	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		if (ex instanceof ExceptionDataHolder) {
			logger.error("exceptionData=" + ((ExceptionDataHolder) ex).getData());
		}
		if (ex instanceof ProcessDataHolder) {
			ProcessData data = ((ProcessDataHolder) ex).getProcessData();
			logger.error("exceptionData=" + data);
			data.setError("Impossible to encode the input image.");
			data.setEncoded(false);
			update(data);
		}
		if (ex.getCause() != null) {
			logger.error("cause=" + ex.getCause());
		}

	}
}
