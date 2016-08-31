package com.auais.note.dao;

import java.util.List;

import com.auais.note.pojo.ImageTag;

public interface ImageTagMapper {

	int deleteByPrimaryKey(String id);

    int insert(ImageTag record);

    int insertSelective(ImageTag record);

    ImageTag selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ImageTag record);

    int updateByPrimaryKeyWithBLOBs(ImageTag record);

    int updateByPrimaryKey(ImageTag record);
    
    List<ImageTag> selectListByIds(String[] ids);
    
    int deleteByIds(List<ImageTag> list);
    
    int deleteByIdsArray(String[] ids);
    
    int batchInsert(List<ImageTag> list);
}