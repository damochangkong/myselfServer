package com.auais.note.dto.inter;

import java.util.Date;
import java.util.List;

public class SyncRequest {
	
	private String userId;
	
	private String deviceId;
	
	private Date sectionSyncTimestamp;

	private List<SyncRequsetNote> notes;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Date getSectionSyncTimestamp() {
		return sectionSyncTimestamp;
	}

	public void setSectionSyncTimestamp(Date sectionSyncTimestamp) {
		this.sectionSyncTimestamp = sectionSyncTimestamp;
	}

	public List<SyncRequsetNote> getNotes() {
		return notes;
	}

	public void setNotes(List<SyncRequsetNote> notes) {
		this.notes = notes;
	}
}
