package com.auais.note.pojo;

import com.auais.note.dto.SectionDto;

public class SectionWithBLOBs extends Section {
    private String text;

    private String imageIds;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    public String getImageIds() {
        return imageIds;
    }

    public void setImageIds(String imageIds) {
        this.imageIds = imageIds == null ? null : imageIds.trim();
    }
    
    @Override
	public boolean equals(Object obj) {
		if(null == obj){
			return false;
		}
		if(!(obj instanceof SectionWithBLOBs)){
			return false;
		}
		SectionWithBLOBs _obj = (SectionWithBLOBs)obj;
		return this.getId().equals(_obj.getId());
	}
    
    public SectionDto convertToSectionDto(){
    	SectionDto sectionDto = new SectionDto();
    	sectionDto.setId(this.getId());
    	sectionDto.setUserId(this.getUserId());
    	sectionDto.setNoteId(this.getNoteId());
    	sectionDto.setUndocFlag(this.getUndocFlag());
    	sectionDto.setSectionType(this.getSectionType());
    	sectionDto.setText(this.getText());
    	sectionDto.setUpdateAt(this.getUpdateAt());
    	sectionDto.setSyncTimestamp(this.getSyncTimestamp());
    	sectionDto.setDeleteFlag(this.getDeleteFlag());
    	return sectionDto;
    }
}