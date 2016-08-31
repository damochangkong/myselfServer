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
     * ���ط���˰汾���ߵĶ���
     * */
    List<SectionWithBLOBs> selectListBySyncTimeAndUserId(@Param("syncTimestamp") String syncTimestamp,
    		@Param("userId") String userId);
    /**
     * ����userId�������еĶ���
     * */
    List<SectionWithBLOBs> selectSectionListByUserId(String userId);
    
    List<SectionWithBLOBs> selectSectionListByNoteId(String noteId);
    
    /**
     * ��������
     * */
    int batchInsert(List<SectionWithBLOBs> list);
    
    /**
     * �����޸�
     * */
    int batchUpdate(List<SectionWithBLOBs> list);
    
    List<SectionWithBLOBs> selectSoruceByUserId(String userId);
    
}