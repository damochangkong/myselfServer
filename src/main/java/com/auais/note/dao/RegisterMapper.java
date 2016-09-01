package com.auais.note.dao;

import com.auais.note.pojo.Register;

public interface RegisterMapper {
	
    int deleteByPrimaryKey(Integer id);

    int insert(Register record);

    int insertSelective(Register record);

    Register selectByPrimaryKey(Integer id);
    
    Register selectByUserName(String userName);

    int updateByPrimaryKeySelective(Register record);

    int updateByPrimaryKey(Register record);
}