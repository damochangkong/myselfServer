package com.auais.note.pojo;

import java.util.Date;

import com.auais.note.dto.NoteDto;
import com.auais.note.dto.inter.CommonFullNote;
import com.auais.note.dto.inter.SyncRequsetNote;

public class Note {
    private String id;

    private String userId;

    private String name;

    private Date createAt;

    private Date updateAt;

    private Date syncTimestamp;

    private Byte modifyFlag;

    private Byte deleteFlag;

    private String deviceId;

    private String childIds;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getChildIds() {
        return childIds;
    }

    public void setChildIds(String childIds) {
        this.childIds = childIds == null ? null : childIds.trim();
    }
    
    public NoteDto convertToNoteDto(){
    	NoteDto _res = new NoteDto();
    	_res.setId(this.getId());
    	_res.setUserId(this.getUserId());
    	_res.setName(this.getName());
    	_res.setSyncTimestamp(this.getSyncTimestamp());
    	_res.setCreateAt(this.getCreateAt());
    	_res.setDeviceId(this.getDeviceId());
    	_res.setDeleteFlag(this.getDeleteFlag());
    	_res.setUpdateAt(this.getUpdateAt());
    	_res.setChildIds(this.getChildIds());
    	return _res;
    }
    
    public CommonFullNote convertToCommonFullNote(){
    	CommonFullNote _res = new CommonFullNote();
    	_res.setId(this.getId());
    	_res.setUserId(this.getUserId());
    	_res.setName(this.getName());
    	_res.setSyncTimestamp(this.getSyncTimestamp());
    	_res.setCreateAt(this.getCreateAt());
    	_res.setDeviceId(this.getDeviceId());
    	_res.setDeleteFlag(this.getDeleteFlag());
    	_res.setModifyFlag(this.getModifyFlag());
    	_res.setUpdateAt(this.getUpdateAt());
    	_res.setChildIds(this.getChildIds());
    	return _res;
    }
    
    public SyncRequsetNote convertToSyncRequsetNote(){
    	SyncRequsetNote _res = new SyncRequsetNote();
    	_res.setNoteId(this.getId());
    	return _res;
    }
    
}