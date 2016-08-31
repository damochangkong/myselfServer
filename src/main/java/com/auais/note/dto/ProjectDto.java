package com.auais.note.dto;

import java.util.List;

public class ProjectDto {
	
	private String userId;
	
	private String deviceId;

	private List<NoteDto> notes;
	
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

	public List<NoteDto> getNotes() {
		return notes;
	}

	public void setNotes(List<NoteDto> notes) {
		this.notes = notes;
	}

	public List<SectionDto> getSections() {
		return sections;
	}

	public void setSections(List<SectionDto> sections) {
		this.sections = sections;
	}
	
}
