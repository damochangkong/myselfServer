package com.auais.note.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.auais.note.pojo.Mind;
import com.auais.note.pojo.MindWithBLOBs;

public interface MindMapper {
    int deleteByPrimaryKey(String id);

    int insert(MindWithBLOBs record);

    int insertSelective(MindWithBLOBs record);

    MindWithBLOBs selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MindWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(MindWithBLOBs record);

    int updateByPrimaryKey(Mind record);
    
    List<MindWithBLOBs> selectMindListByNoteId(@Param("noteId") String noteId);
    
    List<MindWithBLOBs> selectListBySyncTime(String syncTimestamp);
    
    int batchUpdate(List<MindWithBLOBs> list);
    
    int batchInsert(List<MindWithBLOBs> list);
}