package com.auais.note.dto.inter;

public class SyncResponseProject{
	
	private String noteId;
	
	private String code;
	
	private String message;

	private CommonFullNote data;
	
	public String getNoteId() {
		return noteId;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

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
	
	public CommonFullNote getData() {
		return data;
	}

	public void setData(CommonFullNote data) {
		this.data = data;
	}
	
}
