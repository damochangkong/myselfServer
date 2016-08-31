package com.auais.note.pojo;

import java.util.Date;

public class Mind {
    private String id;

    private String noteId;

    private Byte mindType;

    private String name;

    private Date syncTimestamp;
    
    private Date updateAt;

    private Byte modifyFlag;

    private Byte deleteFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId == null ? null : noteId.trim();
    }

    public Byte getMindType() {
        return mindType;
    }

    public void setMindType(Byte mindType) {
        this.mindType = mindType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
    
}