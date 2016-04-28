package org.bellard.bpg.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProcessData implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private Instant created;
	private Instant lastOperation;
	private ProcessAction action;
	private String originalFilename;
	private String visibleOutputFilename;
	private String sessionId;
	private Long bpgSize;
	private Duration encodingDuration;
	private boolean encoded;
	private String error;
	private boolean clientAjaxNotified = false;

	// bpg encoder parameters
	private int bpgQuantizer = 28;
	private int bpgChroma = 420;
	private String bpgColorSpace = "ycbcr";
	private int bpgLevel = 8;
	private int bpgBitDepth = 8;

	private List<String> steps = new ArrayList<String>();

	public ProcessData() {
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public ProcessAction getAction() {
		return action;
	}

	public void setAction(ProcessAction action) {
		this.action = action;
	}

	public Instant getCreated() {
		return created;
	}

	public void setCreated(Instant instant) {
		this.created = instant;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVisibleOutputFilename() {
		return visibleOutputFilename;
	}

	public void setVisibleOutputFilename(String visibleOutputFilename) {
		this.visibleOutputFilename = visibleOutputFilename;
	}

	public Instant getLastOperation() {
		return lastOperation;
	}

	public void setLastOperation(Instant lastOperation) {
		this.lastOperation = lastOperation;
	}

	public List<String> getSteps() {
		return steps;
	}

	public void setSteps(List<String> steps) {
		this.steps = steps;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return "ProcessData [created=" + created + ", lastOperation=" + lastOperation + ", id=" + id + ", action="
				+ action + ", originalFilename=" + originalFilename + ", visibleOutputFilename=" + visibleOutputFilename
				+ ", sessionId=" + sessionId + ", bpgSize=" + bpgSize + ", encodingDuration=" + encodingDuration
				+ ", encoded=" + encoded + ", error=" + error + ", clientAjaxNotified=" + clientAjaxNotified
				+ ", quantizer=" + bpgQuantizer + ", chroma=" + bpgChroma + ", colorSpace=" + bpgColorSpace + ", level="
				+ bpgLevel + ", bitDepth=" + bpgBitDepth + ", steps=" + steps + "]";
	}

	public boolean isEncoded() {
		return encoded;
	}

	public void setEncoded(boolean encoded) {
		this.encoded = encoded;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public boolean isClientAjaxNotified() {
		return clientAjaxNotified;
	}

	public void setClientAjaxNotified(boolean clientAjaxNotified) {
		this.clientAjaxNotified = clientAjaxNotified;
	}

	public Long getBpgSize() {
		return bpgSize;
	}

	public void setBpgSize(Long bpgSize) {
		this.bpgSize = bpgSize;
	}

	public Duration getEncodingDuration() {
		return encodingDuration;
	}

	public void setEncodingDuration(Duration encodingDuration) {
		this.encodingDuration = encodingDuration;
	}

	public int getBpgQuantizer() {
		return bpgQuantizer;
	}

	public void setBpgQuantizer(int quantizer) {
		this.bpgQuantizer = quantizer;
	}

	public int getBpgChroma() {
		return bpgChroma;
	}

	public void setBpgChroma(int chroma) {
		this.bpgChroma = chroma;
	}

	public String getBpgColorSpace() {
		return bpgColorSpace;
	}

	public void setBpgColorSpace(String colorSpace) {
		this.bpgColorSpace = colorSpace;
	}

	public int getBpgLevel() {
		return bpgLevel;
	}

	public void setBpgLevel(int level) {
		this.bpgLevel = level;
	}

	public int getBpgBitDepth() {
		return bpgBitDepth;
	}

	public void setBpgBitDepth(int bitDepth) {
		this.bpgBitDepth = bitDepth;
	}

}
