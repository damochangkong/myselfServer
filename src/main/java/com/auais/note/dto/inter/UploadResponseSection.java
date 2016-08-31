package com.auais.note.dto.inter;

import java.util.Date;

public class UploadResponseSection {

	private String code;

	private String message;

	private Date syncTimestamp;

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

	public Date getSyncTimestamp() {
		return syncTimestamp;
	}

	public void setSyncTimestamp(Date syncTimestamp) {
		this.syncTimestamp = syncTimestamp;
	}

}
