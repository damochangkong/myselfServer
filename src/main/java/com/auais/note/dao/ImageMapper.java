package com.auais.note.dao;

import java.util.List;

import com.auais.note.pojo.Image;

public interface ImageMapper {
	
    int deleteByPrimaryKey(String id);

    int insert(Image record);

    int insertSelective(Image record);

    Image selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Image record);

    int updateByPrimaryKeyWithBLOBs(Image record);

    int updateByPrimaryKey(Image record);
    
    List<Image> selectListByIds(String[] ids);
    
    int deleteByIds(List<Image> list);
    
    int deleteByIdsArray(String[] ids);
    
    int batchInsert(List<Image> list);
    
}