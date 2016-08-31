package com.auais.note.pojo;

import java.util.Date;

public class Section {
    private String id;

    private String userId;

    private String noteId;

    private Byte undocFlag;

    private Byte sectionType;

    private String audioId;

    private Date updateAt;

    private Date syncTimestamp;

    private Byte modifyFlag;

    private Byte deleteFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId == null ? null : noteId.trim();
    }

    public Byte getUndocFlag() {
        return undocFlag;
    }

    public void setUndocFlag(Byte undocFlag) {
        this.undocFlag = undocFlag;
    }

    public Byte getSectionType() {
        return sectionType;
    }

    public void setSectionType(Byte sectionType) {
        this.sectionType = sectionType;
    }

    public String getAudioId() {
        return audioId;
    }

    public void setAudioId(String audioId) {
        this.audioId = audioId == null ? null : audioId.trim();
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
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
}