package com.auais.note.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.auais.note.dao.RegisterMapper;
import com.auais.note.dao.UserMapper;
import com.auais.note.pojo.Register;
import com.auais.note.pojo.User;

@Service
public class UserServiceImpl {
	
	@Resource
	private UserMapper userMapper;
	@Resource
	private RegisterMapper registerMapper;
	
	public long selectMaxUserId(){
		User res = userMapper.selectMaxUserId();
		return res==null?0:res.getUserId();
	}
	
	/**
	 * �����û�����ѯ��¼
	 * 
	 * */
	public User selectByUserName(String userName){
		return userMapper.selectByUserName(userName);
	}
	
	/**
	 * ������������ֻ������ѯ
	 * 
	 * */
	public User selectByEmailOrPhone(String mobile, String email){
		User _param = new User();
		_param.setMobile(mobile);
		_param.setEmail(email);
		return userMapper.selectByEmailOrPhone(_param);
	}

	
	/**
	 * �����û����������ѯ
	 * 
	 * */
	public User selectByUserNameAndPass(String userName,String password){
		User _param = new User();
		_param.setUserName(userName);
		_param.setPassword(password);
		return userMapper.selectByUserNameAndPass(_param);
	}
	
	
	/**
	 * �����û�����ѯ��¼
	 * 
	 * */
	public int insert(User user){
		return userMapper.insert(user);
	}
	
	/**
	 * �����û�����ѯ��¼
	 * 
	 * */
	public User selectByPrimaryKey(long userId){
		return userMapper.selectByPrimaryKey(userId);
	}
	
	public int saveRegister(Register record){
		return this.registerMapper.insert(record);
	}
}
