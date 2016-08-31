package com.auais.note.dao;

import org.apache.ibatis.annotations.Param;

import com.auais.note.pojo.Lock;

public interface LockMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lock record);

    int insertSelective(Lock record);

    Lock selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lock record);

    int updateByPrimaryKey(Lock record);
    
    Lock selectByUserIdAndDeviceId(@Param("userId") String userId,@Param("deviceId") String deviceId);
    
    int deleteByUserIdAndDeviceId(@Param("userId") String userId,@Param("deviceId") String deviceId);
    
}