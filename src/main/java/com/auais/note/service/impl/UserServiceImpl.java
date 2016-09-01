package com.auais.note.service.impl;

import java.util.Date;

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
	public int refreshUser(User user){
		user.setCreateAt(new Date());
		User tableUser = userMapper.selectByUserName(user.getUserName());
		if(null==tableUser){
			return userMapper.insert(user);
		}else{
			user.setUserId(tableUser.getUserId());
			return userMapper.updateByPrimaryKey(user);
		}
	}
	
	/**
	 * �����û�����ѯ��¼
	 * 
	 * */
	public User selectByPrimaryKey(long userId){
		return userMapper.selectByPrimaryKey(userId);
	}
	
	public int updateStatusByUserName(User user){
		return userMapper.updateStatusByUserName(user);
	}
	
	/**
	 * ע��ĵ�һ���ǲ���ע���һ����¼�����߸����������֤��Ϊ���µ�
	 * 
	 * */
	public int refreshRegister(Register record){
		Register registerTemp = this.registerMapper.selectByUserName(record.getUserName());
		if(null != registerTemp){
			record.setId(registerTemp.getId());
			return this.registerMapper.updateByPrimaryKey(record);
		}else{
			return this.registerMapper.insert(record);
		}
	}
	
	public Register selectRegisterByUserName(String userName){
		return this.registerMapper.selectByUserName(userName);
	}
	
	public int deleteRegisterByPrimaryKey(int id){
		return this.registerMapper.deleteByPrimaryKey(id);
	}
	
}
