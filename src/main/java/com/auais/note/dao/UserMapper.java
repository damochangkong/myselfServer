package com.auais.note.dao;

import com.auais.note.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long userId);
    
    User selectByUserName(String userName);
    
    User selectByEmailOrPhone(User record);
    
    User selectByUserNameAndPass(User record);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    User selectMaxUserId();
}