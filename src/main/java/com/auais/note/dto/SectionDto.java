package com.auais.note.dto;

import java.util.Date;
import java.util.List;

import com.auais.note.pojo.SectionWithBLOBs;

public class SectionDto{

    private String id;

    private String userId;

    private String noteId;

    private Byte undocFlag;

    private Byte sectionType;

    private AudioDto audioId;

    private Date updateAt;

    private Date syncTimestamp;

    private Byte deleteFlag;

	private String text;

    private List<ImageDto> imageIds;

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

    public AudioDto getAudioId() {
        return audioId;
    }

    public void setAudioId(AudioDto audioId) {
        this.audioId = audioId;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    public List<ImageDto> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<ImageDto> imageIds) {
        this.imageIds = imageIds;
    }
    
    @Override
	public boolean equals(Object obj) {
		if(null == obj){
			return false;
		}
		if(!(obj instanceof SectionDto)){
			return false;
		}
		SectionDto _obj = (SectionDto)obj;
		return this.getId().equals(_obj.getId());
	}

	public SectionWithBLOBs convertToSectionWithBLOBs(){
		SectionWithBLOBs section = new SectionWithBLOBs();
		section.setAudioId(this.getAudioId().getId());
		section.setId(this.getId());
		section.setUserId(this.getUserId());
		section.setNoteId(this.getNoteId());
		section.setUndocFlag(this.getUndocFlag());
		section.setSectionType(this.getSectionType());
		section.setUpdateAt(this.getUpdateAt());
		section.setSyncTimestamp(this.getSyncTimestamp());
		section.setDeleteFlag(this.getDeleteFlag());
		section.setImageIds(this.getStringImageIds());
		return section;
	}
	
	private String getStringImageIds(){
		StringBuffer _imageIds = new StringBuffer();
		if(null!=this.getImageIds() && this.getImageIds().size()>0){
			for(ImageDto imageDto : this.getImageIds()){
				_imageIds.append(imageDto.getId()).append(",");
			}
			_imageIds.deleteCharAt(_imageIds.length()-1);
			return _imageIds.toString();
		}else{
			return "";
		}
	}
}