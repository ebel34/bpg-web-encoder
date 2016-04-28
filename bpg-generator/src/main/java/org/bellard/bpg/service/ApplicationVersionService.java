package org.bellard.bpg.service;

import org.bellard.bpg.model.AppData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationVersionService {

	@Value("${app.name}")
	private String appName;
	@Value("${app.version}")
	private String appVersion;
	@Value("${build.timestamp}")
	private String buildTimestamp;

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

	public AppData getAppData() {
		return new AppData(appName, appVersion, buildTimestamp);
	}

}
