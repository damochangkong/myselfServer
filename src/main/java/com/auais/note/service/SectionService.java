package com.auais.note.service;

import java.util.Date;
import java.util.List;

import com.auais.note.dto.SectionDto;
import com.auais.note.dto.inter.CommonFullNote;

public interface SectionService {

	List<SectionDto> selectListByTimeAndUserId(Date syncTimestamp,String userId);
	
	List<SectionDto> selectListByNoteId(String userId);
	
	List<SectionDto> selectListByUserId(String userId);
	
	int uploadSaveSectionList(List<SectionDto> sections,String userId,Date syncTime) throws Exception;
	
	void clearSectionAndSource(CommonFullNote reqNote);
	
}
