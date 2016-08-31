package com.auais.note.dto.inter;

import java.util.List;

import com.auais.note.dto.SectionDto;

public class SyncResponse {
	
	private String code;
	
	private String message;
	
	private List<SyncResponseProject> notes;
	
	private List<SectionDto> sections;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<SyncResponseProject> getNotes() {
		return notes;
	}

	public void setNotes(List<SyncResponseProject> notes) {
		this.notes = notes;
	}

	public List<SectionDto> getSections() {
		return sections;
	}

	public void setSections(List<SectionDto> sections) {
		this.sections = sections;
	}
	
}
