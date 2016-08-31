package com.auais.note.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.auais.note.dao.MindMapper;
import com.auais.note.dto.MindDto;
import com.auais.note.pojo.MindWithBLOBs;
import com.auais.note.service.MindService;

@Service("mindService")
public class MindServiceImpl implements MindService {
	@Resource
	private MindMapper mindMapper;
	@Override
	public int insertMind(MindWithBLOBs mind) {
		return mindMapper.insert(mind);
	}

	/**
	 * 批量处理每个项目下面的节点信息
	 * */
	@Override
	public int saveMindList(List<MindDto> mindDtos,String noteId,Date syncTime) throws Exception{
		if(null==mindDtos || mindDtos.size()==0){
			return 0;
		}
		List<MindWithBLOBs> minds = new ArrayList<MindWithBLOBs>();
		MindWithBLOBs _temp = null; 
		for(MindDto mind : mindDtos){
			_temp = mind.convertToMindWithBLOBs();
			_temp.setUpdateAt(new Date());
			minds.add(_temp);
		}
		List<MindWithBLOBs> mindsModify = new ArrayList<MindWithBLOBs>();
		List<MindWithBLOBs> mindsInsert = new ArrayList<MindWithBLOBs>();
		List<MindWithBLOBs> _currMindList = this.mindMapper.selectMindListByNoteId(noteId);
		if(null==_currMindList || _currMindList.isEmpty()){
			return this.mindMapper.batchInsert(minds);
		}
		for(MindWithBLOBs mind : minds){
			mind.setSyncTimestamp(syncTime);
			if(_currMindList.contains(mind)){
				mindsModify.add(mind);
			}else{
				mindsInsert.add(mind);
			}
		}
		int result = 0;
		if(mindsInsert.size()>0){
			result = this.mindMapper.batchInsert(mindsInsert);
		}
		if(mindsModify.size()>0){
			result = result + this.mindMapper.batchUpdate(mindsModify);
		}
		return result;
	}
	
	@Override
	public List<MindDto> getMindListByNoteId(String noteId){
		List<MindDto> minds = new ArrayList<MindDto>();
		List<MindWithBLOBs> _minds = mindMapper.selectMindListByNoteId(noteId);
		for(MindWithBLOBs _curr : _minds){
			minds.add(_curr.convertMindDto());
		}
		return minds;
	}
	
	@Override
	public List<MindDto> getMindListBySyncTime(String syncTime){
		List<MindDto> minds = new ArrayList<MindDto>();
		List<MindWithBLOBs> _minds = mindMapper.selectListBySyncTime(syncTime);
		for(MindWithBLOBs _curr : _minds){
			minds.add(_curr.convertMindDto());
		}
		return minds;
	}
}
