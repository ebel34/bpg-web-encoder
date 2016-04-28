package org.bellard.bpg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackages = "org.bellard.bpg")
@PropertySource("classpath:version.properties")
public class JUnitTestConfiguration {

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

}
