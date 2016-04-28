package org.bellard.bpg.model;

import java.io.Serializable;
import java.time.Duration;

public class UploadedFile implements Serializable {

	private static final long serialVersionUID = 1L;

	private String uuid;
	private String suuid;
	private String name;
	private String bpgname;
	private Integer size;
	private Long bpgSize;
	private String encodingTime;
	private String url;
	private String thumbnail_url;
	private String delete_url;
	private String delete_type;
	private String error;
	private boolean encoded;
	
	public UploadedFile() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnail_url() {
		return thumbnail_url;
	}

	public void setThumbnail_url(String thumbnail_url) {
		this.thumbnail_url = thumbnail_url;
	}

	public String getDelete_url() {
		return delete_url;
	}

	public void setDelete_url(String delete_url) {
		this.delete_url = delete_url;
	}

	public String getDelete_type() {
		return delete_type;
	}

	public void setDelete_type(String delete_type) {
		this.delete_type = delete_type;
	}

	@Override
	public String toString() {
		return "UploadedFile [uuid=" + uuid + ", suuid=" + suuid + ", name=" + name + ", bpgname=" + bpgname
				+ ", size=" + size + ", bpgSize=" + bpgSize + ", encodingTime=" + encodingTime + ", url=" + url
				+ ", thumbnail_url=" + thumbnail_url + ", delete_url=" + delete_url + ", delete_type=" + delete_type
				+ ", error=" + error + ", encoded=" + encoded + "]";
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getBpgname() {
		return bpgname;
	}

	public void setBpgname(String bpgname) {
		this.bpgname = bpgname;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String id) {
		this.uuid = id;
	}

	public String getSuuid() {
		return suuid;
	}

	public void setSuuid(String suuid) {
		this.suuid = suuid;
	}

	public boolean isEncoded() {
		return encoded;
	}

	public void setEncoded(boolean encoded) {
		this.encoded = encoded;
	}

	public Long getBpgSize() {
		return bpgSize;
	}

	public void setBpgSize(Long bpgSize) {
		this.bpgSize = bpgSize;
	}

	public String getEncodingTime() {
		return encodingTime;
	}

	public void setEncodingTime(String encodingTime) {
		this.encodingTime = encodingTime;
	}

}
