package com.auais.note.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.auais.note.dao.ImageTagMapper;
import com.auais.note.pojo.ImageTag;
import com.auais.note.service.ImageTagService;

@Service
public class ImageTagServiceImpl implements ImageTagService{

	@Resource
	private ImageTagMapper imageTagMapper;
	
	
	@Override
	public void batchDeal(List<ImageTag> list) {
		this.imageTagMapper.deleteByIds(list);
		this.imageTagMapper.batchInsert(list);
	}
	
}
