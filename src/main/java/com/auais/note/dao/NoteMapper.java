package com.auais.note.dao;

import java.util.List;

import com.auais.note.pojo.Note;

public interface NoteMapper {
    int deleteByPrimaryKey(String id);

    int insert(Note record);

    int insertSelective(Note record);

    Note selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Note record);

    int updateByPrimaryKeyWithBLOBs(Note record);

    int updateByPrimaryKey(Note record);
    
    List<Note> selectNoteListByUserId(String userId);
    
    int batchInsert(List<Note> list);
    
    int batchUpdate(List<Note> list);
}