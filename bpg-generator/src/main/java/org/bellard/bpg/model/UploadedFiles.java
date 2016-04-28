package org.bellard.bpg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UploadedFiles implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<UploadedFile> files = new ArrayList<UploadedFile>();

	public UploadedFiles() {
	}

	public List<UploadedFile> getFiles() {
		return files;
	}

	public void setFiles(List<UploadedFile> files) {
		this.files = files;
	}

}
