package com.auais.note.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auais.note.dao.MindMapper;
import com.auais.note.dao.SectionMapper;
import com.auais.note.pojo.MindWithBLOBs;
import com.auais.note.pojo.User;
import com.auais.note.service.ImageService;
import com.auais.note.service.MindService;
import com.auais.note.service.NoteService;
import com.auais.note.service.SectionService;
import com.auais.note.service.impl.UserServiceImpl;
import com.auais.note.util.AuaisConstans;
import com.auais.note.util.DateUtils;
import com.auais.note.util.PropertyContain;
import com.auais.note.util.SHAUtil;
import com.auais.note.util.SqlCheckUtils;
import com.auais.note.util.Token;
import com.auais.note.util.TokenUtil;

@Controller
@RequestMapping("/test")
public class TestController extends BaseController{
	
	private static Logger logger = Logger.getLogger(TestController.class);
	@Resource
	private ImageService imageService;
	@Resource
	private MindService mindService;
	@Resource
	private MindMapper mindMapper;
	@Resource
	private NoteService noteService;
	@Resource
	private SectionService sectionService;
	@Resource
	private SectionMapper sectionMapper;
	@Resource
	private UserServiceImpl userServiceImpl;
	@Resource
	private PropertyContain propertyContain;
	
	@RequestMapping("/sync")
	public void testSync(HttpServletRequest request,HttpServletResponse response,Model model){
		System.out.println("------------queryNoteInfo------------");
		logger.info("test1 begin");
		MindWithBLOBs mind = new MindWithBLOBs();
		mind.setId("mind1004");
		mind.setNoteId("note1002");
		mind.setMindType((byte)0);
		mind.setName("MindB");
		mind.setChildIds("a,b,c");
		mind.setSectionIds("s1,s2,s3");
		mind.setSyncTimestamp(new Date());
		mind.setModifyFlag((byte)0);
		mind.setDeleteFlag((byte)0);
		this.mindService.insertMind(mind);
		logger.info("test1 success");
	}
	
	@RequestMapping("/querySection")
	public void querySection(HttpServletRequest request,HttpServletResponse response,Model model){
		
//		SectionWithBLOBs record = new SectionWithBLOBs();
//		record.setSyncTimestamp(DateUtils.parser("2016-08-12 09:02:15"));
//		List<SectionWithBLOBs> sectionList = this.sectionMapper.selectSectionListBySyncTimestamp(record);
//		logger.info(sectionList.size());
//		logger.info(gson.toJson(sectionList));
		
//		List<SectionDto> sectionDtoList = sectionService.selectListByTimeAndUserId(DateUtils.parser("2016-08-12 17:52:15"));
//		System.out.println(gson.toJson(sectionDtoList));
		
	}
	
	@RequestMapping("/queryMind")
	public void queryMind(HttpServletRequest request,HttpServletResponse response,Model model){
		
		List<MindWithBLOBs> minds = mindMapper.selectListBySyncTime(DateUtils.formatDateStr(new Date()));
		System.out.println(gson.toJson(minds));
		
	}
	
	@RequestMapping("/testProperty")
	public void test(HttpServletRequest request,HttpServletResponse response,Model model){
		
		System.out.println(propertyContain.getAccessKey());
		
	}
	/**
	 * �û���¼
	 * 
	 * */
	@ResponseBody
	@RequestMapping("logout")
	public String logout(@RequestParam("userId") String userId,
			@RequestParam("signature") String signature,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		response.setCharacterEncoding("UTF-8"); //���ñ����ʽ
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, String> resultMap = new HashMap<String, String>();
		if(!NumberUtils.isDigits(userId)){
			resultMap.put("code", "1001");
			resultMap.put("message", "�û�id����");
			return gson.toJson(resultMap);
		}
		if(TokenUtil.validateToken(signature, Long.valueOf(userId))){
			TokenUtil.removeToken(Long.valueOf(userId));
		}else{
			resultMap.put("code", "0002");
			resultMap.put("message", "token��Ч");
			return gson.toJson(resultMap);
		}
		resultMap.put("code", "0000");
		resultMap.put("message", "�����˳�");
		return gson.toJson(resultMap);
	}
	
	/**
	 * �û���¼
	 * 
	 * */
	@ResponseBody
	@RequestMapping("login")
	public String login(@RequestParam("userId") String userId, 
			@RequestParam("password") String password,@RequestParam("deviceId") String deviceId,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		response.setCharacterEncoding("UTF-8"); //���ñ����ʽ
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("code", "0000");
		if (SqlCheckUtils.sqlInject(userId)) {
			resultMap.put("code", "1001");
			resultMap.put("message", "�û����ƺ��Ƿ��ַ�");
			return gson.toJson(resultMap);
		}
		if (SqlCheckUtils.sqlInject(password)) {
			resultMap.put("code", "1002");
			resultMap.put("message", "���뺬�Ƿ��ַ�");
			return gson.toJson(resultMap);
		}
		// ��У���û��Ƿ����
		User _user = userServiceImpl.selectByUserName(userId);
		if(null== _user || StringUtils.isEmpty(String.valueOf(_user.getUserId()))){
			resultMap.put("code", "1004");
			resultMap.put("message", "�û�������");
			return gson.toJson(resultMap);
		}
		// ��У�������Ƿ���ȷ
		password = SHAUtil.getResult(password);
		_user = userServiceImpl.selectByUserNameAndPass(userId, password);
		if(null== _user || StringUtils.isEmpty(String.valueOf(_user.getUserId()))){
			resultMap.put("code", "1005");
			resultMap.put("message", "�������");
			return gson.toJson(resultMap);
		}
		//����token
		Token token = TokenUtil.generateToken(deviceId, _user.getUserId());
		resultMap.put("token", token.getSignature());
		resultMap.put("message", "��¼�ɹ�");
		return gson.toJson(resultMap);
	}
	
	@ResponseBody
	@RequestMapping("registerOld")
	public String registerOld(String userName, @RequestParam("password") String password,
			String passwordCon, @RequestParam("deviceId") String deviceId,
			@RequestParam("mobile") String mobile, String email, String userPhoto,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		response.setCharacterEncoding("UTF-8"); //���ñ����ʽ
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("code", "0000");
		User user = new User();
		user.setUserName(StringUtils.isEmpty(userName)?mobile:userName);
		user.setPassword(password);
		user.setPasswordCon(passwordCon);
		user.setMobile(mobile);
		user.setEmail(email);
		user.setUserPhoto(userPhoto);
		user.setDeviceId(deviceId);
		//У�����
		resultMap = this.validateRegisterInfoOld(resultMap, user);
		if (!StringUtils.equals(resultMap.get("code"), "0000")) {
			return gson.toJson(resultMap);
		}
		//����user��¼
		user.setPassword(SHAUtil.getResult(password));
		//����userId
		long userId = userServiceImpl.selectMaxUserId();
		userId = userId==0?AuaisConstans.INIT_USERID:(userId+1);
		user.setUserId(userId);
		int res = this.userServiceImpl.insert(user);
		User tableUser = null;
		if(res == 0){
			resultMap.put("code", "4001");
			resultMap.put("message", "�����ʧ��");
			return gson.toJson(resultMap);
		}else{
			tableUser = this.userServiceImpl.selectByUserName(userName);
		}
		//����token
		Token token = TokenUtil.generateToken(deviceId, tableUser.getUserId());
		resultMap.put("userId", String.valueOf(tableUser.getUserId()));
		resultMap.put("token", token.getSignature());
		resultMap.put("message", "ע��ɹ�");
		return gson.toJson(resultMap);
	}

	/**
	 * ��ע�����ν���У��
	 * 
	 * */
	private Map<String, String> validateRegisterInfoOld(Map<String, String> resultMap, User user) {
		resultMap.put("code", "0000");
		if (SqlCheckUtils.sqlInject(user.getUserName())) {
			resultMap.put("code", "1001");
			resultMap.put("message", "�û����ƺ��Ƿ��ַ�");
			return resultMap;
		}
		int userNameLength = this.getWordCount(user.getUserName());
		if (userNameLength > 20 || userNameLength < 4) {
			resultMap.put("code", "1002");
			resultMap.put("message", "�û����Ƴ��Ȳ���4��20֮��");
			return resultMap;
		}
		//У���û����Ƿ��Ѿ�����
		User _user = userServiceImpl.selectByUserName(user.getUserName());
		if(null!=_user && StringUtils.isNotEmpty(String.valueOf(_user.getUserId()))){
			resultMap.put("code", "1003");
			resultMap.put("message", "�û������Ѿ�����");
			return resultMap;			
		}
		if (SqlCheckUtils.sqlInject(user.getPassword())) {
			resultMap.put("code", "2001");
			resultMap.put("message", "���뺬�Ƿ��ַ�");
			return resultMap;
		}
		int passwordLength = this.getWordCount(user.getPassword());
		if (passwordLength > 20 || passwordLength < 6) {
			resultMap.put("code", "2002");
			resultMap.put("message", "���볤�Ȳ���6��32֮��");
			return resultMap;
		}
		if (!StringUtils.equals(user.getPassword(), user.getPasswordCon())) {
			resultMap.put("code", "2003");
			resultMap.put("message", "�û�������������벻һ��");
			return resultMap;
		}
		if (StringUtils.isEmpty(user.getEmail()) && StringUtils.isEmpty(user.getMobile())) {
			resultMap.put("code", "3001");
			resultMap.put("message", "����͵绰����������һ������Ϊ��");
			return resultMap;
		}
		if(StringUtils.isNotEmpty(user.getEmail())){
			if (!user.getEmail().matches("\\w+@\\w+(\\.\\w+)+")) {
				resultMap.put("code", "3002");
				resultMap.put("message", "�����ʽ����");
				return resultMap;
			}
			_user = userServiceImpl.selectByEmailOrPhone(null,user.getEmail());
			if(null!=_user && StringUtils.isNotEmpty(_user.getEmail())){
				resultMap.put("code", "3003");
				resultMap.put("message", "�����Ѿ�ע����");
				return resultMap;
			}
		}
		if(StringUtils.isNotEmpty(user.getMobile())){
			_user = userServiceImpl.selectByEmailOrPhone(user.getMobile(),null);
			if(!this.isPhoneNumber(user.getMobile())){
				resultMap.put("code", "3004");
				resultMap.put("message", "�ֻ������ʽ����");
				return resultMap;				
			}
			if(null!=_user && StringUtils.isNotEmpty(_user.getMobile())){
				resultMap.put("code", "3005");
				resultMap.put("message", "�ֻ������Ѿ�ע����");
				return resultMap;
			}
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
		
		
		
	}
	
}
