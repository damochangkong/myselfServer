package com.auais.note.dto.inter;

import java.util.List;

import com.auais.note.dto.MindDto;
import com.auais.note.dto.SectionDto;
import com.auais.note.pojo.Note;

public class CommonFullNote extends Note{
	
	private List<MindDto> minds;
	
	private List<SectionDto> sections;

	public List<MindDto> getMinds() {
		return minds;
	}

	public void setMinds(List<MindDto> minds) {
		this.minds = minds;
	}

	public List<SectionDto> getSections() {
		return sections;
	}

	public void setSections(List<SectionDto> sections) {
		this.sections = sections;
	}
	
	public Note convertToNote(){
		Note _curr = new Note();
		_curr.setId(this.getId());
		_curr.setUserId(this.getUserId());
		_curr.setDeviceId(this.getDeviceId());
		_curr.setCreateAt(this.getCreateAt());
		_curr.setDeleteFlag(this.getDeleteFlag());
		_curr.setModifyFlag(this.getModifyFlag());
		_curr.setName(this.getName());
		_curr.setUpdateAt(this.getUpdateAt());
		return _curr;
	}
}
