package org.bellard.bpg.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AppSession {

	private String sessionId;
	private int maxProcessData;
	private Map<String, ProcessData> pmap = new HashMap<String, ProcessData>();

	public AppSession(String sessionId, int maxProcessData) {
		this.sessionId = sessionId;
		this.maxProcessData = maxProcessData;
	}

	public ProcessData getProcessData(String pid) {
		return pmap.get(pid);
	}

	public boolean putProcessData(String pid, ProcessData data) {
		if (pmap.containsKey(pid) || pmap.values().size() <= maxProcessData) {
			pmap.put(pid, data);
			return true;
		}
		return false;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getMaxProcessData() {
		return maxProcessData;
	}

	public void setMaxProcessData(int maxProcessData) {
		this.maxProcessData = maxProcessData;
	}

	public Collection<ProcessData> getProcessDatas() {
		return pmap.values();
	}
	
}
