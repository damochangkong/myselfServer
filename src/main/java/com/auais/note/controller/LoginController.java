package com.auais.note.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auais.note.pojo.Register;
import com.auais.note.pojo.User;
import com.auais.note.service.impl.UserServiceImpl;
import com.auais.note.util.AuaisConstans;
import com.auais.note.util.DateUtils;
import com.auais.note.util.SHAUtil;
import com.auais.note.util.SqlCheckUtils;
import com.auais.note.util.Token;
import com.auais.note.util.TokenUtil;
import com.auais.note.util.mail.MailSendUtil;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

	@Resource
	private UserServiceImpl userServiceImpl;
	
	@ResponseBody
	@RequestMapping("register")
	public String register(@RequestParam("userName") String userName,
			@RequestParam("password") String password,
			@RequestParam("deviceId") String deviceId,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		response.setCharacterEncoding("UTF-8"); //设置编码格式
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("code", "0000");
		resultMap = this.validateRegisterInfo(resultMap, userName,password,deviceId);
		if (!StringUtils.equals(resultMap.get("code"), "0000")) {
			return gson.toJson(resultMap);
		}
		User user = new User();
		user.setUserName(userName);
		user.setEmail(userName);
		user.setDeviceId(deviceId);
		user.setPassword(SHAUtil.getResult(password));
		user.setStatus((byte)0);
		long userId = userServiceImpl.selectMaxUserId();
		userId = (userId==0)?AuaisConstans.INIT_USERID:(userId+1);
		user.setUserId(userId);
		//发送邮件验证码
		resultMap = MailSendUtil.sendEmail(userName);
		if(!StringUtils.equals(resultMap.get("code"), "0000")){
			resultMap.put("code", "3003");
			resultMap.put("message", "邮件发送失败，请确认邮箱是否正确");
			return gson.toJson(resultMap);
		}
		String smsCode = resultMap.get("smsCode");
		resultMap.remove("smsCode");
		//保存 userName,smsCode,time
		Register register = new Register();
		register.setSmsCode(smsCode);
		register.setUserName(userName);
		register.setSmsTime(new Date());
		this.userServiceImpl.refreshRegister(register);
		int res = 0;
		try {
			res = this.userServiceImpl.refreshUser(user);
		} catch (Exception e) {
			resultMap.put("code", "3001");
			resultMap.put("message", "插入表失败");
			return gson.toJson(resultMap);
		}
		if(res == 0){
			resultMap.put("code", "3001");
			resultMap.put("message", "插入表失败");
			return gson.toJson(resultMap);
		}
		resultMap.put("message", "注册第一步通过");
		return gson.toJson(resultMap);
	}
	
	@ResponseBody
	@RequestMapping("validateEmail")
	public String validateEmail(@RequestParam("smsCode") String smsCode,
			@RequestParam("userName") String userName,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		response.setCharacterEncoding("UTF-8"); //设置编码格式
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("code", "0000");
		resultMap = this.validateSmsCode(resultMap, userName, smsCode);
		if (!StringUtils.equals(resultMap.get("code"), "0000")) {
			return gson.toJson(resultMap);
		}
		//直接去找到记录
		User tableUser = this.userServiceImpl.selectByUserName(userName);
		if(null == tableUser){
			resultMap.put("code", "1005");
			resultMap.put("message", "用户不存在");
			return gson.toJson(resultMap);
		}
		if(tableUser.getStatus()==1){
			resultMap.put("code", "1006");
			resultMap.put("message", "用户已经成功注册");
			return gson.toJson(resultMap);
		}
		resultMap.put("userId", String.valueOf(tableUser.getUserId()));
		//去临时表中查找对应的记录，存在即通过
		Register register = this.userServiceImpl.selectRegisterByUserName(userName);
		resultMap = this.validateSmsCodelegal(register, smsCode);
		if(!StringUtils.equals(resultMap.get("code"), "0000")){
			resultMap.put("code", "1007");
			return gson.toJson(resultMap);
		}
		//修改user表中的状态为1
		tableUser.setStatus((byte)1);
		if(this.userServiceImpl.updateStatusByUserName(tableUser)==0){
			resultMap.put("code", "1008");
			resultMap.put("message", "用户状态更新失败");
			return gson.toJson(resultMap);
		}
		//清空注册临时表中的记录
		this.userServiceImpl.deleteRegisterByPrimaryKey(register.getId());
		//生成token
		Token token = TokenUtil.generateToken(tableUser.getDeviceId(), tableUser.getUserId());
		resultMap.put("token", token.getSignature());
		resultMap.put("message", "注册成功");
		return gson.toJson(resultMap);
	}

	/**
	 * 用户登录
	 * 
	 * */
	@ResponseBody
	@RequestMapping("login")
	public String login(@RequestParam("userName") String userName, 
			@RequestParam("password") String password,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		response.setCharacterEncoding("UTF-8"); //设置编码格式
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("code", "0000");
		resultMap = this.validateLoginParam(userName, password);
		if (!StringUtils.equals(resultMap.get("code"), "0000")) {
			return gson.toJson(resultMap);
		}
		// 再校验密码是否正确
		password = SHAUtil.getResult(password);
		User _user = this.userServiceImpl.selectByUserNameAndPass(userName, password);
		if(null == _user){
			resultMap.put("code", "1005");
			resultMap.put("message", "密码错误");
			return gson.toJson(resultMap);
		}
		resultMap.put("userId", String.valueOf(_user.getUserId()));
		//生成token
		Token token = TokenUtil.generateToken(_user.getDeviceId(), _user.getUserId());
		resultMap.put("token", token.getSignature());
		resultMap.put("message", "登录成功");
		return gson.toJson(resultMap);
	}
	
	/**
	 * 校验登录的参数
	 * 
	 * **/
	private Map<String,String> validateLoginParam(String userName,String password){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("code", "0000");
		if (SqlCheckUtils.sqlInject(userName)) {
			resultMap.put("code", "1001");
			resultMap.put("message", "用户名称含非法字符");
			return resultMap;
		}
		if (SqlCheckUtils.sqlInject(password)) {
			resultMap.put("code", "1002");
			resultMap.put("message", "密码含非法字符");
			return resultMap;
		}
		// 先校验用户是否存在
		User _user = userServiceImpl.selectByUserName(userName);
		if(null== _user){
			resultMap.put("code", "1004");
			resultMap.put("message", "用户不存在");
			return resultMap;
		}
		if(_user.getStatus() != 1){
			resultMap.put("code", "1005");
			resultMap.put("message", "用户尚未激活");
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 用户登录
	 * 
	 * */
	@ResponseBody
	@RequestMapping("logout")
	public String logout(@RequestParam("userName") String userName,
			@RequestParam("token") String token,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		response.setCharacterEncoding("UTF-8"); //设置编码格式
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("code", "0000");
		User user = this.userServiceImpl.selectByUserName(userName);
		if(null == user){
			resultMap.put("code", "1001");
			resultMap.put("message", "用户不存在");
			return gson.toJson(resultMap);
		}
		if(user.getStatus()!=1){
			resultMap.put("code", "1002");
			resultMap.put("message", "用户未激活");
			return gson.toJson(resultMap);			
		}
		if(TokenUtil.validateToken(token, user.getUserId())){
			TokenUtil.removeToken(user.getUserId());
		}else{
			resultMap.put("code", "1003");
			resultMap.put("message", "token无效");
			return gson.toJson(resultMap);
		}
		resultMap.put("message", "您已退出");
		return gson.toJson(resultMap);
	}
	
	/**
	 * 验证验证码是否正确且有效
	 * 
	 * 
	 * **/
	private Map<String,String> validateSmsCodelegal(Register register,String smscode){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("code", "0000");
		Date smsTime = register.getSmsTime();
		long seconds = DateUtils.getDistanceTime(new Date(), smsTime);
		if(seconds > 1800){
			resultMap.put("code", "1001");
			resultMap.put("message", "验证码已经失效");
		}
		if(!StringUtils.equals(register.getSmsCode(), smscode)){
			resultMap.put("code", "1002");
			resultMap.put("message", "验证码错误");
		}
		return resultMap;
	}
	
	/**
	 * 验证邮箱参数校验
	 * 
	 * **/	
	private Map<String, String> validateSmsCode(Map<String, String> resultMap,
			String userName,String smsCode){
		resultMap.put("code", "0000");
		if(StringUtils.isEmpty(userName)){
			resultMap.put("code", "1000");
			resultMap.put("message", "用户名称不能为空");
			return resultMap;
		}
		if(StringUtils.isEmpty(smsCode)){
			resultMap.put("code", "1001");
			resultMap.put("message", "验证码不能为空");
			return resultMap;
		}
		if (SqlCheckUtils.sqlInject(userName)) {
			resultMap.put("code", "1002");
			resultMap.put("message", "用户名称含非法字符");
			return resultMap;
		}
		if (!userName.matches("\\w+@\\w+(\\.\\w+)+")) {
			resultMap.put("code", "1003");
			resultMap.put("message", "邮箱格式错误");
			return resultMap;
		}
		if (StringUtils.length(smsCode)!=6) {
			resultMap.put("code", "1004");
			resultMap.put("message", "验证码是6位，请确认");
			return resultMap;
		}
		
		return resultMap;
	}
	/**
	 * 注册参数校验
	 * 
	 * **/
	private Map<String, String> validateRegisterInfo(Map<String, String> resultMap,
			String userName,String password,String deviceId) {
		resultMap.put("code", "0000");
		if(StringUtils.isEmpty(userName)){
			resultMap.put("code", "1000");
			resultMap.put("message", "用户名称不能为空");
			return resultMap;
		}
		if(StringUtils.isEmpty(password)){
			resultMap.put("code", "2000");
			resultMap.put("message", "密码不能为空");
			return resultMap;
		}
		if(StringUtils.isEmpty(deviceId)){
			resultMap.put("code", "2003");
			resultMap.put("message", "设备ID不能为空");
			return resultMap;			
		}
		if (SqlCheckUtils.sqlInject(userName)) {
			resultMap.put("code", "1001");
			resultMap.put("message", "用户名称含非法字符");
			return resultMap;
		}
		int userNameLength = this.getWordCount(userName);
		if (userNameLength > 20 || userNameLength < 4) {
			resultMap.put("code", "1002");
			resultMap.put("message", "用户名称长度不在4到20之间");
			return resultMap;
		}
		if (SqlCheckUtils.sqlInject(password)) {
			resultMap.put("code", "2001");
			resultMap.put("message", "密码含非法字符");
			return resultMap;
		}
		int passwordLength = this.getWordCount(password);
		if (passwordLength > 20 || passwordLength < 6) {
			resultMap.put("code", "2002");
			resultMap.put("message", "密码长度不在6到32之间");
			return resultMap;
		}
		//校验用户名是否已经存在
		User _user = userServiceImpl.selectByUserName(userName);
		if(null!=_user && _user.getStatus()==1){
			resultMap.put("code", "1003");
			resultMap.put("message", "用户名称已经存在");
			return resultMap;
		}
		return resultMap;
	}
	
	/**
	 * 获取字符的长度
	 * */
	private int getWordCount(String s) {
		int length = 0;
		for (int i = 0; i < s.length(); i++) {
			int ascii = Character.codePointAt(s, i);
			if (ascii >= 0 && ascii <= 255)
				length++;
			else
				length += 2;
		}
		return length;
	}

	/**
	 * 校验是否手机号码
	 * */
	private boolean isPhoneNumber(String input){
	    Pattern pattern = Pattern.compile("^(1[0-9])\\d{9}$");
	    return pattern.matcher(input).matches();
	}
	
	public static void main(String[] args) {
		LoginController ss = new LoginController();
		System.out.println(ss.isPhoneNumber("17095239558"));
	}
}
