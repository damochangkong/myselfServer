package com.auais.note.dto;

import java.util.Date;
import java.util.List;

import com.auais.note.pojo.MindWithBLOBs;

public class NoteDto {

    private String id;

    private String userId;

    private String name;

    private Date createAt;

    private Date updateAt;

    private Date syncTimestamp;

    private Byte deleteFlag;
    
    private List<MindWithBLOBs> minds;
    
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

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

	public List<MindWithBLOBs> getMinds() {
		return minds;
	}

	public void setMinds(List<MindWithBLOBs> minds) {
		this.minds = minds;
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getChildIds() {
		return childIds;
	}

	public void setChildIds(String childIds) {
		this.childIds = childIds;
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("equals");
		if(null == obj){
			return false;
		}
		if(!(obj instanceof NoteDto)){
			return false;
		}
		NoteDto _obj = (NoteDto)obj;
		return this.getId().equals(_obj.getId());
	}
	
}