package org.bellard.bpg.exception;

import org.bellard.bpg.model.ExceptionData;
import org.bellard.bpg.model.ProcessData;

public class TechnicalException extends RuntimeException implements ExceptionDataHolder, ProcessDataHolder {

	private static final long serialVersionUID = 1L;

	private ExceptionData data;
	private ProcessData processData;
	private String message;

	public TechnicalException(ExceptionData data, String message) {
		super();
		this.data = data;
		this.message = message;
	}

	public TechnicalException(ExceptionData data) {
		super();
		this.data = data;
	}

	public TechnicalException(ExceptionData data, Exception cause) {
		super(cause);
		this.data = data;
	}

	public TechnicalException(ExceptionData data, ProcessData processData) {
		super();
		this.data = data;
		this.processData = processData;
	}

	public TechnicalException(ExceptionData data, ProcessData processData, Exception cause) {
		super(cause);
		this.data = data;
		this.processData = processData;
	}

	public ExceptionData getData() {
		return data;
	}

	public void setData(ExceptionData data) {
		this.data = data;
	}

	public ProcessData getProcessData() {
		return processData;
	}

	public void setProcessData(ProcessData processData) {
		this.processData = processData;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
