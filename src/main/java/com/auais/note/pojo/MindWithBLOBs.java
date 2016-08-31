package com.auais.note.pojo;

import com.auais.note.dto.MindDto;

public class MindWithBLOBs extends Mind {
    private String childIds;

    private String sectionIds;

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
    
	@Override
	public boolean equals(Object obj) {
		if(null == obj){
			return false;
		}
		if(!(obj instanceof MindWithBLOBs)){
			return false;
		}
		MindWithBLOBs _obj = (MindWithBLOBs)obj;
		return this.getId().equals(_obj.getId());
	}
	
    public MindDto convertMindDto(){
    	MindDto mindDto = new MindDto();
    	mindDto.setChildIds(this.getChildIds());
    	mindDto.setDeleteFlag(this.getDeleteFlag());
    	mindDto.setId(this.getId());
    	mindDto.setMindType(this.getMindType());
    	mindDto.setName(this.getName());
    	mindDto.setNoteId(this.getNoteId());
    	mindDto.setSectionIds(this.getSectionIds());
    	mindDto.setSyncTimestamp(this.getSyncTimestamp());
    	return mindDto;
    }
}