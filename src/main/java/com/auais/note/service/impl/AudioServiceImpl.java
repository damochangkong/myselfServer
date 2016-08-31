package com.auais.note.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.auais.note.dao.AudioMapper;
import com.auais.note.dao.AudioTagMapper;
import com.auais.note.dao.SectionMapper;
import com.auais.note.dto.AudioDto;
import com.auais.note.dto.SectionDto;
import com.auais.note.pojo.Audio;
import com.auais.note.pojo.AudioTag;
import com.auais.note.pojo.SectionWithBLOBs;
import com.auais.note.pojo.Source;
import com.auais.note.service.AudioService;
import com.auais.note.util.AuaisConstans;

@Service
public class AudioServiceImpl implements AudioService {

	@Resource
	private AudioMapper audioMapper;
	@Resource
	private AudioTagMapper audioTagMapper;
	@Resource
	private SectionMapper sectionMapper;
	
	@Override
	public AudioDto organizeAudio(String audioId) {
		//�õ�audio�������
		Audio audio = audioMapper.selectByPrimaryKey(audioId);
		if(null == audio){
			return new AudioDto();
		}
		AudioDto audioDto = audio.convertToAudioDto();
		String audioTagIds = audio.getAudioTagIds();
		//�õ�tags����
		if(!StringUtils.isEmpty(audioTagIds)){
			List<AudioTag> audioTags =  audioTagMapper.selectListByIds(StringUtils.split(audioTagIds, ","));
			audioDto.setAudioTagIds(audioTags);
		}
		return audioDto;
	}

	/**
	 * ����һ����Ƶ��¼
	 * ��ɾ���������ƵTAG
	 * */
	@Override
	public void dealAudio(AudioDto audioDto,SectionDto sectionDto) {
		this.deleteTableSource(null, sectionDto.getId());
		//�����ɾ�����䣬ֱ�ӷ�����
		if(sectionDto.getDeleteFlag()==1){
			return ;
		}
		List<AudioTag> audioTagIds = audioDto.getAudioTagIds();
		this.audioTagMapper.deleteByIds(audioTagIds);
		this.audioTagMapper.batchInsert(audioTagIds);
		this.audioMapper.deleteByPrimaryKey(audioDto.getId());
		this.audioMapper.insert(audioDto.convert2Audio());
	}
	
	/**
	 * ��ɾ��������ļ�¼
	 * 
	 * */
	private void deleteTableSource(SectionWithBLOBs section,String sectionId){
		if(null == section){
			section = this.sectionMapper.selectByPrimaryKey(sectionId);
		}
		if(null == section){
			return;
		}
		String tableAudioId = section.getAudioId();
		if(StringUtils.isNotEmpty(tableAudioId)){
			Audio audio = this.audioMapper.selectByPrimaryKey(tableAudioId);
			if(null != audio){
				if(StringUtils.isNotEmpty(audio.getAudioTagIds())){
					String[] tags = StringUtils.split(audio.getAudioTagIds(), ",");
					this.audioTagMapper.deleteByIdsArray(tags);
				}
			}
			this.audioMapper.deleteByPrimaryKey(tableAudioId);
		}
	}
	
	@Override
	public void clearSource(SectionWithBLOBs section){
		this.deleteTableSource(section, null);
	}
	
	/**
	 * ��֯��Դ
	 * 
	 * */
	@Override
	public List<Source> collectSource(SectionWithBLOBs tableSection, List<Source> source){
		String userId = tableSection.getUserId();
		Audio audio = this.audioMapper.selectByPrimaryKey(tableSection.getAudioId());
		if(null != audio){
			Source _source = null;
			if(StringUtils.isNotEmpty(audio.getAudioTagIds())){
				String[] tags = StringUtils.split(audio.getAudioTagIds(), ",");
				 List<AudioTag> audioTags = this.audioTagMapper.selectListByIds(tags);
				 if(null!=audioTags && !audioTags.isEmpty()){
					 for(AudioTag _curr : audioTags){
						 if(_curr.getTagType() != AuaisConstans.AUDIO_TAG_TYPE_TEXT){
							 _source = _curr.convertToSource();
							 _source.setUserId(userId);
							 source.add(_source);
						 }
					 }
				 }
			}
			_source = audio.convertToSource();
			 _source.setUserId(userId);
			 source.add(_source);
		}
		return source;
	}
}
