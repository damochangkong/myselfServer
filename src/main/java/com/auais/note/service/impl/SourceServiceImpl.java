package com.auais.note.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.auais.note.controller.BaseController;
import com.auais.note.dao.SectionMapper;
import com.auais.note.dao.SourceMapper;
import com.auais.note.pojo.SectionWithBLOBs;
import com.auais.note.pojo.Source;
import com.auais.note.service.AudioService;
import com.auais.note.service.ImageService;
import com.auais.note.util.AuaisConstans;

@Service
public class SourceServiceImpl {

	static Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	@Resource
	private SectionMapper sectionMapper;
	@Resource
	private AudioService audioService;
	@Resource
	private ImageService imageService;
	@Resource
	private SourceMapper sourceMapper;
	
	/**
	 * 整理现在的流量表
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public void updateSourceAfterUpload(String userId){
		List<Source> newList = this.organizeSourceByUserId(userId);
		List<Source> oldList = this.sourceMapper.selectListByUserId(userId);
		//已经被删除的
		List<Source> oldOnly = ListUtils.removeAll(oldList, newList);
		//在现有的表中不存在的
		List<Source> newOnly = ListUtils.removeAll(newList, oldList);
		//删除已经没有引用的
		if(null!=oldOnly && !oldOnly.isEmpty()){
			int del = this.sourceMapper.deleteListByIds(oldOnly);
			logger.info("删除的条数：" + del);
		}
		//增加新的
		if(null!=newOnly && !newOnly.isEmpty()){
			int ins = this.sourceMapper.batchInsert(newOnly);
			logger.info("增加的条数：" + ins);
		}
	}
	
	/**
	 * 组织一个用户下的所有的段落的资源
	 * */
	private List<Source> organizeSourceByUserId(String userId){
		List<Source> list = new ArrayList<Source>();
		List<SectionWithBLOBs> sections = this.sectionMapper.selectSoruceByUserId(userId);
		for(SectionWithBLOBs _curr : sections){
			if(_curr.getSectionType()==AuaisConstans.SECTION_TYPE_IMAGE){
				if(StringUtils.isNotEmpty(_curr.getImageIds())){
					//先处理掉里面的资源信息
					this.imageService.collectSource(_curr,list);
				}
			}
			if(_curr.getSectionType()==AuaisConstans.SECTION_TYPE_AUDIO){
				//先处理掉里面的资源信息
				this.audioService.collectSource(_curr,list);
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		List<String> newA = new ArrayList<String>();
		List<String> oldS = new ArrayList<String>();
		newA.add("a1");
		newA.add("a2");
		newA.add("a3");
		oldS.add("a3");
		oldS.add("a4");	
		List<String> newOnly = ListUtils.removeAll(newA, oldS);//[a1, a2]
		System.out.println(newOnly);
		List<String> oldOnly = ListUtils.removeAll(oldS, newA);//[a4]
		System.out.println(oldOnly);
	}
	
}
