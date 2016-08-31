package com.auais.note.dto.inter;

import java.util.Date;

public class SyncRequsetNote {

	private String noteId;
	
	private Date syncTimestamp;
	
	private Byte modifyFlag;
	
	public String getNoteId() {
		return noteId;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	public Date getSyncTimestamp() {
		return syncTimestamp;
	}

	public void setSyncTimestamp(Date syncTimestamp) {
		this.syncTimestamp = syncTimestamp;
	}

	public Byte getModifyFlag() {
		return modifyFlag;
	}

	public void setModifyFlag(Byte modifyFlag) {
		this.modifyFlag = modifyFlag;
	}

	@Override
	public boolean equals(Object obj) {
		if(null == obj){
			return false;
		}
		if(!(obj instanceof SyncRequsetNote)){
			return false;
		}
		SyncRequsetNote _obj = (SyncRequsetNote)obj;
		if(this != obj){
			return this.getNoteId().equals(_obj.getNoteId());
		}
		return true;
	}
}
