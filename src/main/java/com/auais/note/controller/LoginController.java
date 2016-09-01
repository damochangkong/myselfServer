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
		response.setCharacterEncoding("UTF-8"); //���ñ����ʽ
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
		//�����ʼ���֤��
		resultMap = MailSendUtil.sendEmail(userName);
		if(!StringUtils.equals(resultMap.get("code"), "0000")){
			resultMap.put("code", "3003");
			resultMap.put("message", "�ʼ�����ʧ�ܣ���ȷ�������Ƿ���ȷ");
			return gson.toJson(resultMap);
		}
		String smsCode = resultMap.get("smsCode");
		resultMap.remove("smsCode");
		//���� userName,smsCode,time
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
			resultMap.put("message", "�����ʧ��");
			return gson.toJson(resultMap);
		}
		if(res == 0){
			resultMap.put("code", "3001");
			resultMap.put("message", "�����ʧ��");
			return gson.toJson(resultMap);
		}
		resultMap.put("message", "ע���һ��ͨ��");
		return gson.toJson(resultMap);
	}
	
	@ResponseBody
	@RequestMapping("validateEmail")
	public String validateEmail(@RequestParam("smsCode") String smsCode,
			@RequestParam("userName") String userName,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		response.setCharacterEncoding("UTF-8"); //���ñ����ʽ
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("code", "0000");
		resultMap = this.validateSmsCode(resultMap, userName, smsCode);
		if (!StringUtils.equals(resultMap.get("code"), "0000")) {
			return gson.toJson(resultMap);
		}
		//ֱ��ȥ�ҵ���¼
		User tableUser = this.userServiceImpl.selectByUserName(userName);
		if(null == tableUser){
			resultMap.put("code", "1005");
			resultMap.put("message", "�û�������");
			return gson.toJson(resultMap);
		}
		if(tableUser.getStatus()==1){
			resultMap.put("code", "1006");
			resultMap.put("message", "�û��Ѿ��ɹ�ע��");
			return gson.toJson(resultMap);
		}
		resultMap.put("userId", String.valueOf(tableUser.getUserId()));
		//ȥ��ʱ���в��Ҷ�Ӧ�ļ�¼�����ڼ�ͨ��
		Register register = this.userServiceImpl.selectRegisterByUserName(userName);
		resultMap = this.validateSmsCodelegal(register, smsCode);
		if(!StringUtils.equals(resultMap.get("code"), "0000")){
			resultMap.put("code", "1007");
			return gson.toJson(resultMap);
		}
		//�޸�user���е�״̬Ϊ1
		tableUser.setStatus((byte)1);
		if(this.userServiceImpl.updateStatusByUserName(tableUser)==0){
			resultMap.put("code", "1008");
			resultMap.put("message", "�û�״̬����ʧ��");
			return gson.toJson(resultMap);
		}
		//���ע����ʱ���еļ�¼
		this.userServiceImpl.deleteRegisterByPrimaryKey(register.getId());
		//����token
		Token token = TokenUtil.generateToken(tableUser.getDeviceId(), tableUser.getUserId());
		resultMap.put("token", token.getSignature());
		resultMap.put("message", "ע��ɹ�");
		return gson.toJson(resultMap);
	}

	/**
	 * �û���¼
	 * 
	 * */
	@ResponseBody
	@RequestMapping("login")
	public String login(@RequestParam("userName") String userName, 
			@RequestParam("password") String password,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		response.setCharacterEncoding("UTF-8"); //���ñ����ʽ
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("code", "0000");
		resultMap = this.validateLoginParam(userName, password);
		if (!StringUtils.equals(resultMap.get("code"), "0000")) {
			return gson.toJson(resultMap);
		}
		// ��У�������Ƿ���ȷ
		password = SHAUtil.getResult(password);
		User _user = this.userServiceImpl.selectByUserNameAndPass(userName, password);
		if(null == _user){
			resultMap.put("code", "1005");
			resultMap.put("message", "�������");
			return gson.toJson(resultMap);
		}
		resultMap.put("userId", String.valueOf(_user.getUserId()));
		//����token
		Token token = TokenUtil.generateToken(_user.getDeviceId(), _user.getUserId());
		resultMap.put("token", token.getSignature());
		resultMap.put("message", "��¼�ɹ�");
		return gson.toJson(resultMap);
	}
	
	/**
	 * У���¼�Ĳ���
	 * 
	 * **/
	private Map<String,String> validateLoginParam(String userName,String password){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("code", "0000");
		if (SqlCheckUtils.sqlInject(userName)) {
			resultMap.put("code", "1001");
			resultMap.put("message", "�û����ƺ��Ƿ��ַ�");
			return resultMap;
		}
		if (SqlCheckUtils.sqlInject(password)) {
			resultMap.put("code", "1002");
			resultMap.put("message", "���뺬�Ƿ��ַ�");
			return resultMap;
		}
		// ��У���û��Ƿ����
		User _user = userServiceImpl.selectByUserName(userName);
		if(null== _user){
			resultMap.put("code", "1004");
			resultMap.put("message", "�û�������");
			return resultMap;
		}
		if(_user.getStatus() != 1){
			resultMap.put("code", "1005");
			resultMap.put("message", "�û���δ����");
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * �û���¼
	 * 
	 * */
	@ResponseBody
	@RequestMapping("logout")
	public String logout(@RequestParam("userName") String userName,
			@RequestParam("token") String token,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		response.setCharacterEncoding("UTF-8"); //���ñ����ʽ
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("code", "0000");
		User user = this.userServiceImpl.selectByUserName(userName);
		if(null == user){
			resultMap.put("code", "1001");
			resultMap.put("message", "�û�������");
			return gson.toJson(resultMap);
		}
		if(user.getStatus()!=1){
			resultMap.put("code", "1002");
			resultMap.put("message", "�û�δ����");
			return gson.toJson(resultMap);			
		}
		if(TokenUtil.validateToken(token, user.getUserId())){
			TokenUtil.removeToken(user.getUserId());
		}else{
			resultMap.put("code", "1003");
			resultMap.put("message", "token��Ч");
			return gson.toJson(resultMap);
		}
		resultMap.put("message", "�����˳�");
		return gson.toJson(resultMap);
	}
	
	/**
	 * ��֤��֤���Ƿ���ȷ����Ч
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
			resultMap.put("message", "��֤���Ѿ�ʧЧ");
		}
		if(!StringUtils.equals(register.getSmsCode(), smscode)){
			resultMap.put("code", "1002");
			resultMap.put("message", "��֤�����");
		}
		return resultMap;
	}
	
	/**
	 * ��֤�������У��
	 * 
	 * **/	
	private Map<String, String> validateSmsCode(Map<String, String> resultMap,
			String userName,String smsCode){
		resultMap.put("code", "0000");
		if(StringUtils.isEmpty(userName)){
			resultMap.put("code", "1000");
			resultMap.put("message", "�û����Ʋ���Ϊ��");
			return resultMap;
		}
		if(StringUtils.isEmpty(smsCode)){
			resultMap.put("code", "1001");
			resultMap.put("message", "��֤�벻��Ϊ��");
			return resultMap;
		}
		if (SqlCheckUtils.sqlInject(userName)) {
			resultMap.put("code", "1002");
			resultMap.put("message", "�û����ƺ��Ƿ��ַ�");
			return resultMap;
		}
		if (!userName.matches("\\w+@\\w+(\\.\\w+)+")) {
			resultMap.put("code", "1003");
			resultMap.put("message", "�����ʽ����");
			return resultMap;
		}
		if (StringUtils.length(smsCode)!=6) {
			resultMap.put("code", "1004");
			resultMap.put("message", "��֤����6λ����ȷ��");
			return resultMap;
		}
		
		return resultMap;
	}
	/**
	 * ע�����У��
	 * 
	 * **/
	private Map<String, String> validateRegisterInfo(Map<String, String> resultMap,
			String userName,String password,String deviceId) {
		resultMap.put("code", "0000");
		if(StringUtils.isEmpty(userName)){
			resultMap.put("code", "1000");
			resultMap.put("message", "�û����Ʋ���Ϊ��");
			return resultMap;
		}
		if(StringUtils.isEmpty(password)){
			resultMap.put("code", "2000");
			resultMap.put("message", "���벻��Ϊ��");
			return resultMap;
		}
		if(StringUtils.isEmpty(deviceId)){
			resultMap.put("code", "2003");
			resultMap.put("message", "�豸ID����Ϊ��");
			return resultMap;			
		}
		if (SqlCheckUtils.sqlInject(userName)) {
			resultMap.put("code", "1001");
			resultMap.put("message", "�û����ƺ��Ƿ��ַ�");
			return resultMap;
		}
		int userNameLength = this.getWordCount(userName);
		if (userNameLength > 20 || userNameLength < 4) {
			resultMap.put("code", "1002");
			resultMap.put("message", "�û����Ƴ��Ȳ���4��20֮��");
			return resultMap;
		}
		if (SqlCheckUtils.sqlInject(password)) {
			resultMap.put("code", "2001");
			resultMap.put("message", "���뺬�Ƿ��ַ�");
			return resultMap;
		}
		int passwordLength = this.getWordCount(password);
		if (passwordLength > 20 || passwordLength < 6) {
			resultMap.put("code", "2002");
			resultMap.put("message", "���볤�Ȳ���6��32֮��");
			return resultMap;
		}
		//У���û����Ƿ��Ѿ�����
		User _user = userServiceImpl.selectByUserName(userName);
		if(null!=_user && _user.getStatus()==1){
			resultMap.put("code", "1003");
			resultMap.put("message", "�û������Ѿ�����");
			return resultMap;
		}
		return resultMap;
	}
	
	/**
	 * ��ȡ�ַ��ĳ���
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
	 * У���Ƿ��ֻ�����
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
