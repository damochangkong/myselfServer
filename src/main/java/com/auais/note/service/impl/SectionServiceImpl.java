package com.auais.note.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.auais.note.dao.SectionMapper;
import com.auais.note.dto.SectionDto;
import com.auais.note.dto.inter.CommonFullNote;
import com.auais.note.pojo.SectionWithBLOBs;
import com.auais.note.service.AudioService;
import com.auais.note.service.ImageService;
import com.auais.note.service.ImageTagService;
import com.auais.note.service.SectionService;
import com.auais.note.util.AuaisConstans;
import com.auais.note.util.DateUtils;

@Service
public class SectionServiceImpl implements SectionService {

	@Resource
	private SectionMapper sectionMapper;
	@Resource
	private AudioService audioService;
	@Resource
	private ImageService imageService;
	@Resource 
	private ImageTagService imageTagService;
	
	/**
	 * ��������section��Ϣ
	 * */
	public int uploadSaveSectionList(List<SectionDto> sections,String userId,Date syncTime) throws Exception{
		if(null==sections || sections.size()==0){
			return 0;
		}
		List<SectionWithBLOBs> sectionWithBLOBs = new ArrayList<SectionWithBLOBs>();
		for(SectionDto _curr : sections){
			sectionWithBLOBs.add(_curr.convertToSectionWithBLOBs());
			if(_curr.getSectionType()==AuaisConstans.SECTION_TYPE_IMAGE){
				if(null!=_curr.getImageIds() && _curr.getImageIds().size()>0){
					//�ȴ�����������Դ��Ϣ
					imageService.batchDealImages(_curr.getImageIds(),_curr);
				}
			}
			if(_curr.getSectionType()==AuaisConstans.SECTION_TYPE_AUDIO){
				//�ȴ�����������Դ��Ϣ
				audioService.dealAudio(_curr.getAudioId(),_curr);
			}
		}
		List<SectionWithBLOBs> _sections = sectionMapper.selectSectionListByUserId(userId);
		if(null== _sections || _sections.isEmpty()){
			return this.sectionMapper.batchInsert(sectionWithBLOBs);
		}
		List<SectionWithBLOBs> sectionsModify = new ArrayList<SectionWithBLOBs>();
		List<SectionWithBLOBs> sectionsInsert = new ArrayList<SectionWithBLOBs>();
		for(SectionWithBLOBs _curr : sectionWithBLOBs){
			_curr.setSyncTimestamp(syncTime);
			if(_sections.contains(_curr)){
				sectionsModify.add(_curr);
			}else{
				sectionsInsert.add(_curr);
			}
		}
		int res = 0;
		if(sectionsInsert.size()>0){
			res = this.sectionMapper.batchInsert(sectionsInsert);
		}
		if(sectionsModify.size()>0){
			res = res + this.sectionMapper.batchUpdate(sectionsModify);
		}
		return res;
	}
	
	/**
	 * ��ն����е�ͼƬ���ú���������
	 * ͬʱ��ն�Ӧ����Դ��¼
	 * 
	 * */
	@Override
	public void clearSectionAndSource(CommonFullNote reqNote){
		List<SectionWithBLOBs> sections = this.sectionMapper.selectSectionListByNoteId(reqNote.getId());
		for(SectionWithBLOBs _curr : sections){
			if(_curr.getSectionType()==AuaisConstans.SECTION_TYPE_IMAGE){
				if(StringUtils.isNotEmpty(_curr.getImageIds())){
					//�ȴ�����������Դ��Ϣ
					this.imageService.clearSource(_curr);
				}
			}
			if(_curr.getSectionType()==AuaisConstans.SECTION_TYPE_AUDIO){
				//�ȴ�����������Դ��Ϣ
				this.audioService.clearSource(_curr);
			}
		}
	}
	
	/**
	 * ����noteId
	 * ������֯������Ϣ
	 * */
	@Override
	public List<SectionDto> selectListByNoteId(String noteId) {
		List<SectionWithBLOBs> result = this.sectionMapper.selectSectionListByNoteId(noteId);
		return this.organizeSectionDtoList(result);
	}
	
	/**
	 * ����userId
	 * ������֯������Ϣ
	 * */
	@Override
	public List<SectionDto> selectListByUserId(String userId) {
		List<SectionWithBLOBs> result = this.sectionMapper.selectSectionListByUserId(userId);
		return this.organizeSectionDtoList(result);
	}
	
	/**
	 * �õ����еİ汾���ߵĶ���
	 * 
	 * */
	@Override
	public List<SectionDto> selectListByTimeAndUserId(Date syncTimestamp,String userId) {
		String syncTime = DateUtils.formatDateStr(syncTimestamp);
		List<SectionWithBLOBs> result = this.sectionMapper.selectListBySyncTimeAndUserId(syncTime,userId);
		if(result.size()==0){
			return new ArrayList<SectionDto>();
		}
		List<SectionDto> sectionDtoList = new ArrayList<SectionDto>();
		for(SectionWithBLOBs _curr : result){
			sectionDtoList.add(this.organizeSectionDto(_curr));
		}
		return sectionDtoList;
	}
	
	/**
	 * ������֯������Ϣ
	 * */
	private List<SectionDto> organizeSectionDtoList(List<SectionWithBLOBs> result){
		if(result.size()==0){
			return new ArrayList<SectionDto>();
		}
		List<SectionDto> sectionDtoList = new ArrayList<SectionDto>();
		for(SectionWithBLOBs _curr : result){
			sectionDtoList.add(this.organizeSectionDto(_curr));
		}
		return sectionDtoList;
	}
	
	/**
	 * ��֯һ��SectionDto
	 * */
	private SectionDto organizeSectionDto(SectionWithBLOBs _section){
		SectionDto _temp = _section.convertToSectionDto();
		//��ԃ�����l���е���Ϣ
		if(_section.getSectionType() == AuaisConstans.SECTION_TYPE_AUDIO){
			_temp.setAudioId(audioService.organizeAudio(_section.getAudioId()));
		}
		//��ԃ���DƬ���е���Ϣ
		if(_section.getSectionType() == AuaisConstans.SECTION_TYPE_IMAGE){
			String[] imageIds = StringUtils.split(_section.getImageIds(), ",");
			if(imageIds.length>0){
				_temp.setImageIds(imageService.organizeImageDto(imageIds));
			}
		}
		return _temp;
	}
	
}
