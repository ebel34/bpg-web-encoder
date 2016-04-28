package org.bellard.bpg.exception;

import org.bellard.bpg.model.ExceptionData;
import org.bellard.bpg.model.ProcessData;

public class BusinessException extends Exception implements ProcessDataHolder, ExceptionDataHolder {

	private static final long serialVersionUID = 1L;

	private ExceptionData data;
	private ProcessData processData;

	public BusinessException(ExceptionData data) {
		super();
		this.data = data;
	}

	public BusinessException(ExceptionData data, Exception e) {
		super(e);
		this.data = data;
	}

	public BusinessException(ExceptionData data, ProcessData processData) {
		super();
		this.data = data;
		this.processData = processData;
	}

	public BusinessException(ExceptionData data, ProcessData processData, Exception e) {
		super(e);
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

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		if (super.getMessage() != null) {
			sb.append(super.getMessage() + " : ");
		}
		sb.append(toString());
		return sb.toString();
	}

	@Override
	public String toString() {
		return "BusinessException [data=" + data + ", processData=" + processData + ", getCause()=" + getCause() + "]";
	}

}
