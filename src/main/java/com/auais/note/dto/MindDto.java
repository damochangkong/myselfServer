package com.auais.note.dto;

import java.util.Date;

import com.auais.note.pojo.MindWithBLOBs;

public class MindDto{

    private String id;

    private String noteId;

    private Byte mindType;

    private String name;

    private Date syncTimestamp;
    
    private Date updateAt;

    private Byte deleteFlag;
    
	private String childIds;

    private String sectionIds;


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

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    
    public String getChildIds() {
        return childIds;
    }

    public void setChildIds(String childIds) {
        this.childIds = childIds == null ? null : childIds.trim();
    }

    public String getSectionIds() {
        return sectionIds;
    }

    public void setSectionIds(String sectionIds) {
        this.sectionIds = sectionIds == null ? null : sectionIds.trim();
    }
    
	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
    
    public MindWithBLOBs convertToMindWithBLOBs(){
    	MindWithBLOBs _curr = new MindWithBLOBs();
    	_curr.setChildIds(this.getChildIds());
    	_curr.setDeleteFlag(this.getDeleteFlag());
    	_curr.setId(this.getId());
    	_curr.setMindType(this.getMindType());
    	_curr.setName(this.getName());
    	_curr.setNoteId(this.getNoteId());
    	_curr.setSectionIds(this.getSectionIds());
    	_curr.setSyncTimestamp(this.getSyncTimestamp());
    	_curr.setUpdateAt(this.getUpdateAt());
    	return _curr;
    }
}