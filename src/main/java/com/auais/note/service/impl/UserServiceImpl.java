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
	 * 按照用户名查询记录
	 * 
	 * */
	public User selectByUserName(String userName){
		return userMapper.selectByUserName(userName);
	}
	
	/**
	 * 按照邮箱或者手机号码查询
	 * 
	 * */
	public User selectByEmailOrPhone(String mobile, String email){
		User _param = new User();
		_param.setMobile(mobile);
		_param.setEmail(email);
		return userMapper.selectByEmailOrPhone(_param);
	}

	
	/**
	 * 按照用户名和密码查询
	 * 
	 * */
	public User selectByUserNameAndPass(String userName,String password){
		User _param = new User();
		_param.setUserName(userName);
		_param.setPassword(password);
		return userMapper.selectByUserNameAndPass(_param);
	}
	
	
	/**
	 * 按照用户名查询记录
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
	 * 按照用户名查询记录
	 * 
	 * */
	public User selectByPrimaryKey(long userId){
		return userMapper.selectByPrimaryKey(userId);
	}
	
	public int updateStatusByUserName(User user){
		return userMapper.updateStatusByUserName(user);
	}
	
	/**
	 * 注册的第一步是插入注册表一条记录，或者更新里面的验证码为最新的
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
