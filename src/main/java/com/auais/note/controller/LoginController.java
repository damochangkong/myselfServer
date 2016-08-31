package com.auais.note.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auais.note.pojo.Register;
import com.auais.note.pojo.User;
import com.auais.note.service.impl.UserServiceImpl;
import com.auais.note.util.AuaisConstans;
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
			@RequestParam("password") String password,@RequestParam("deviceId") String deviceId,
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
		int res = 0;
		try {
			res = this.userServiceImpl.insert(user);
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
		resultMap = MailSendUtil.sendEmail(userName);
		if(!StringUtils.equals(resultMap.get("code"), "0000")){
			resultMap.put("code", "3003");
			resultMap.put("message", "邮件发送失败，请确认邮箱是否正确");
			return gson.toJson(resultMap);
		}
		String smsCode = resultMap.get("smsCode");
		//保存 userName,smsCode,time
		Register register = new Register();
		register.setSmsCode(smsCode);
		register.setUserName(userName);
		register.setSmsTime(new Date());
		this.userServiceImpl.saveRegister(register);
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
			resultMap.put("code", "1002");
			resultMap.put("message", "用户不存在");
			return gson.toJson(resultMap);
		}
		resultMap.put("userId", String.valueOf(tableUser.getUserId()));
		//去临时表中查找对应的记录，存在即通过
		
		//修改user表中的状态为1
		
		//生成token
		Token token = TokenUtil.generateToken(tableUser.getDeviceId(), tableUser.getUserId());
		
		resultMap.put("token", token.getSignature());
		return gson.toJson(resultMap);
	}	

	/**
	 * 用户登录
	 * 
	 * */
	@ResponseBody
	@RequestMapping("login")
	public String login(@RequestParam("userId") String userId, 
			@RequestParam("password") String password,@RequestParam("deviceId") String deviceId,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		response.setCharacterEncoding("UTF-8"); //设置编码格式
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("code", "0000");
		if (SqlCheckUtils.sqlInject(userId)) {
			resultMap.put("code", "1001");
			resultMap.put("message", "用户名称含非法字符");
			return gson.toJson(resultMap);
		}
		if (SqlCheckUtils.sqlInject(password)) {
			resultMap.put("code", "1002");
			resultMap.put("message", "密码含非法字符");
			return gson.toJson(resultMap);
		}
		// 先校验用户是否存在
		User _user = userServiceImpl.selectByUserName(userId);
		if(null== _user || StringUtils.isEmpty(String.valueOf(_user.getUserId()))){
			resultMap.put("code", "1004");
			resultMap.put("message", "用户不存在");
			return gson.toJson(resultMap);
		}
		// 再校验密码是否正确
		password = SHAUtil.getResult(password);
		_user = userServiceImpl.selectByUserNameAndPass(userId, password);
		if(null== _user || StringUtils.isEmpty(String.valueOf(_user.getUserId()))){
			resultMap.put("code", "1005");
			resultMap.put("message", "密码错误");
			return gson.toJson(resultMap);
		}
		//生成token
		Token token = TokenUtil.generateToken(deviceId, _user.getUserId());
		resultMap.put("token", token.getSignature());
		resultMap.put("message", "登录成功");
		return gson.toJson(resultMap);
	}

	/**
	 * 用户登录
	 * 
	 * */
	@ResponseBody
	@RequestMapping("logout")
	public String logout(@RequestParam("userId") String userId,
			@RequestParam("signature") String signature,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		response.setCharacterEncoding("UTF-8"); //设置编码格式
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, String> resultMap = new HashMap<String, String>();
		if(!NumberUtils.isDigits(userId)){
			resultMap.put("code", "1001");
			resultMap.put("message", "用户id错误");
			return gson.toJson(resultMap);
		}
		if(TokenUtil.validateToken(signature, Long.valueOf(userId))){
			TokenUtil.removeToken(Long.valueOf(userId));
		}else{
			resultMap.put("code", "0002");
			resultMap.put("message", "token无效");
			return gson.toJson(resultMap);
		}
		resultMap.put("code", "0000");
		resultMap.put("message", "您已退出");
		return gson.toJson(resultMap);
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
			resultMap.put("code", "2000");
			resultMap.put("message", "验证码不能为空");
			return resultMap;
		}
		if (SqlCheckUtils.sqlInject(userName)) {
			resultMap.put("code", "1001");
			resultMap.put("message", "用户名称含非法字符");
			return resultMap;
		}
		if (!userName.matches("\\w+@\\w+(\\.\\w+)+")) {
			resultMap.put("code", "3002");
			resultMap.put("message", "邮箱格式错误");
			return resultMap;
		}
		if (StringUtils.length(smsCode)!=6) {
			resultMap.put("code", "2002");
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
		if(null!=_user && StringUtils.isNotEmpty(String.valueOf(_user.getUserId()))){
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
