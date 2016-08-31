package com.auais.note.dao;

import com.auais.note.pojo.Audio;

public interface AudioMapper {
    int deleteByPrimaryKey(String id);

    int insert(Audio record);

    int insertSelective(Audio record);

    Audio selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Audio record);

    int updateByPrimaryKeyWithBLOBs(Audio record);

    int updateByPrimaryKey(Audio record);
    
}