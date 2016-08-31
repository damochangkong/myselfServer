package com.auais.note.service;

import com.auais.note.pojo.Lock;

public interface LockService {

	
	//1.ÄÃµ½Ëø
	boolean hasHoldLock(String userId,String deviceId);
	
	//2.Ëø
	int addLock(Lock lock);
	
	//3.½âËø
	int deleteLock(String userId,String deviceId);
	
	
	boolean isLocked(String userId, String deviceId);
	
}
