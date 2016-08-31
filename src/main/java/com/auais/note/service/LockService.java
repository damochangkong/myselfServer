package com.auais.note.service;

import com.auais.note.pojo.Lock;

public interface LockService {

	
	//1.�õ���
	boolean hasHoldLock(String userId,String deviceId);
	
	//2.��
	int addLock(Lock lock);
	
	//3.����
	int deleteLock(String userId,String deviceId);
	
	
	boolean isLocked(String userId, String deviceId);
	
}
