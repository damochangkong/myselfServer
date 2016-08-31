package com.auais.note.service;

import java.util.Date;
import java.util.List;

import com.auais.note.dto.MindDto;
import com.auais.note.pojo.MindWithBLOBs;

public interface MindService {

	public int insertMind(MindWithBLOBs mind);
	
	int saveMindList(List<MindDto> mindDtos,String userId,Date syncTime) throws Exception;
	 
	List<MindDto> getMindListByNoteId(String noteId);
	
	List<MindDto> getMindListBySyncTime(String syncTime);
}
