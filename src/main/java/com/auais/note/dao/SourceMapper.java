package com.auais.note.dao;

import java.util.List;

import com.auais.note.pojo.Source;

public interface SourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Source record);

    int insertSelective(Source record);

    Source selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Source record);

    int updateByPrimaryKey(Source record);
    
    List<Source> selectListByUserId(String userId);

    int deleteListByIds(List<Source> list);

    int batchInsert(List<Source> list);
    
}