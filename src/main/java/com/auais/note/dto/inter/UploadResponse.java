package com.auais.note.dto.inter;

import java.util.List;

public class UploadResponse {

	private String code;

	private String message;

	private UploadResponseSection section;

	private List<UploadResponseNote> notes;

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

	public UploadResponseSection getSection() {
		return section;
	}

	public void setSection(UploadResponseSection section) {
		this.section = section;
	}

	public List<UploadResponseNote> getNotes() {
		return notes;
	}

	public void setNotes(List<UploadResponseNote> notes) {
		this.notes = notes;
	}

}
