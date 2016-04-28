package org.bellard.bpg.model;

public class AppData {
	private String appName;
	private String appVersion;
	private String buildTimestamp;

	public AppData(String appName, String appVersion, String buildTimestamp) {
		super();
		this.appName = appName;
		this.appVersion = appVersion;
		this.buildTimestamp = buildTimestamp;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getBuildTimestamp() {
		return buildTimestamp;
	}

	public void setBuildTimestamp(String buildTimestamp) {
		this.buildTimestamp = buildTimestamp;
	}

}
