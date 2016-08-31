package com.auais.note.dto.inter;

import java.util.List;

import com.auais.note.dto.SectionDto;

public class UploadRequest{

	private String userId;
	
	private String deviceId;
	
	private List<CommonFullNote> notes;
	
	private List<SectionDto> sections;

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

	public List<CommonFullNote> getNotes() {
		return notes;
	}

	public void setNotes(List<CommonFullNote> notes) {
		this.notes = notes;
	}
	
	public List<SectionDto> getSections() {
		return sections;
	}

	public void setSections(List<SectionDto> sections) {
		this.sections = sections;
	}
}
