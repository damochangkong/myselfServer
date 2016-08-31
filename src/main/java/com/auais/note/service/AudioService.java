package com.auais.note.service;

import java.util.List;

import com.auais.note.dto.AudioDto;
import com.auais.note.dto.SectionDto;
import com.auais.note.pojo.SectionWithBLOBs;
import com.auais.note.pojo.Source;

public interface AudioService {

	AudioDto organizeAudio(String audioId);
	
	
	void dealAudio(AudioDto audioDto,SectionDto sectionDto);
	
	void clearSource(SectionWithBLOBs section);
	
	List<Source> collectSource(SectionWithBLOBs tableSection, List<Source> source);
	
}
