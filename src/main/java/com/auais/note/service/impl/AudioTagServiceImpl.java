package com.auais.note.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.auais.note.dao.AudioTagMapper;
import com.auais.note.pojo.AudioTag;
import com.auais.note.service.AudioTagService;

@Service
public class AudioTagServiceImpl implements AudioTagService{

	@Resource
	private AudioTagMapper audioTagMapper;
	
	@Override
	public void batchDeal(List<AudioTag> list) {
		this.audioTagMapper.deleteByIds(list);
		this.audioTagMapper.batchInsert(list);
	}
	
}
