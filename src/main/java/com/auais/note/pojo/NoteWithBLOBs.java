package com.auais.note.pojo;

public class NoteWithBLOBs extends Note {
	
    private String childIds;
    
    public String getChildIds() {
        return childIds;
    }

    public void setChildIds(String childIds) {
        this.childIds = childIds == null ? null : childIds.trim();
    }
}