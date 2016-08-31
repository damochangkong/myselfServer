package com.auais.note.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.auais.note.dao.LockMapper;
import com.auais.note.pojo.Lock;
import com.auais.note.service.LockService;
import com.auais.note.util.DateUtils;

@Service
public class LockServiceImpl implements LockService {

	@Resource
	private LockMapper lockMapper;
	
	/**
	 * 獲得鎖
	 * */
	@Override
	public boolean hasHoldLock(String userId, String deviceId) {
		Lock lockDto = this.lockMapper.selectByUserIdAndDeviceId(userId, deviceId);
		if(null == lockDto){
			return false;
		}else{
			Date createDate = lockDto.getCreateAt();
			long seconds = DateUtils.getDistanceTime(new Date(), createDate);
			if(seconds > 60){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 加鎖
	 * */
	@Override
	public int addLock(Lock lock) {
		this.lockMapper.deleteByUserIdAndDeviceId(lock.getUserId(),null);
		return this.lockMapper.insert(lock);
	}

	/**
	 * 去除鎖
	 * */	
	@Override
	public int deleteLock(String userId, String deviceId) {
		return this.lockMapper.deleteByUserIdAndDeviceId(userId, null);
	}
	
	/**
	 * 判斷是否被鎖
	 * */
	public boolean isLocked(String userId, String deviceId){
		Lock lockDto = this.lockMapper.selectByUserIdAndDeviceId(userId, null);
		if(null == lockDto){
			return false;
		}else{
			Date createDate = lockDto.getCreateAt();
			long seconds = DateUtils.getDistanceTime(new Date(), createDate);
			if(seconds > 60){
				return false;
			}
		}
		return true;
	}

}
