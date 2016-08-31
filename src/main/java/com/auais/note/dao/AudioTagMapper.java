package com.auais.note.dao;

import java.util.List;

import com.auais.note.pojo.AudioTag;

public interface AudioTagMapper {
	
    int deleteByPrimaryKey(String id);

    int insert(AudioTag record);

    int insertSelective(AudioTag record);

    AudioTag selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AudioTag record);

    int updateByPrimaryKeyWithBLOBs(AudioTag record);

    int updateByPrimaryKey(AudioTag record);
    
    List<AudioTag> selectListByIds(String[] ids);
    
    int deleteByIds(List<AudioTag> list);
    
    int deleteByIdsArray(String[] ids);
    
    int batchInsert(List<AudioTag> list);
    
}