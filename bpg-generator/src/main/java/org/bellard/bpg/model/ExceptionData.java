package org.bellard.bpg.model;

import java.io.Serializable;

public enum ExceptionData implements Serializable {

	RESOURCE_CREATE("T100", "Problem when creating a resource"), 
	RESOURCE_DELETE("T101", "Problem when deleting a resource"), 
	RESOURCE_DOWNLOAD("T102", "Problem when downloading a resource"), 
	RESOURCE_NO_DOWNLOAD("T102", "No resource to download"), 
	RESOURCE_UPLOAD("T103", "Problem when uploading a resource"), 
	SPRING_BATCH_ISE("T200", "Spring batch illegal state exception"), 
	JSON_MAPPING_ERROR("T300", "Problem during json2Objec or object2Json transformation"), 
	THREAD_ENCODING_ERROR("T300", "Problem with thread during encoding"), 
	BPG_COMMAND_PARAMETER_ERROR("400", "Problem with the encoding command parameter"), 

	BPGENC_ERROR("F100", "Problem with the bpegenc encoding process"),
	INPUT_IMAGE_CHECK_ERROR("F200", "Problem with the input message"),
	MAX_FILE_PER_SESSION_EXCEED("F300", "The maximum file upload for this session has been reached. Please update the web page and identify a new captcha."),
	NO_SESSION("F301", "The session does not exist."),
	NO_PROCESS_DATA("F301", "The process data does not exist.");

	private String code;
	private String message;

	private ExceptionData(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "ExceptionData[code=" + code + ",message=" + message + "]";
	}

}
