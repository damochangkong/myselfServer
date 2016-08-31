package com.auais.note.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.auais.note.pojo.Section;
import com.auais.note.pojo.SectionWithBLOBs;

public interface SectionMapper {
	
    int deleteByPrimaryKey(String id);

    int insert(SectionWithBLOBs record);

    int insertSelective(SectionWithBLOBs record);

    SectionWithBLOBs selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SectionWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SectionWithBLOBs record);

    int updateByPrimaryKey(Section record);
    
    /**
     * 返回服务端版本更高的段落
     * */
    List<SectionWithBLOBs> selectListBySyncTimeAndUserId(@Param("syncTimestamp") String syncTimestamp,
    		@Param("userId") String userId);
    /**
     * 根据userId返回所有的段落
     * */
    List<SectionWithBLOBs> selectSectionListByUserId(String userId);
    
    List<SectionWithBLOBs> selectSectionListByNoteId(String noteId);
    
    /**
     * 批量增加
     * */
    int batchInsert(List<SectionWithBLOBs> list);
    
    /**
     * 批量修改
     * */
    int batchUpdate(List<SectionWithBLOBs> list);
    
    List<SectionWithBLOBs> selectSoruceByUserId(String userId);
    
}