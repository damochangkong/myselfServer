package com.auais.note.util.mail;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import com.mysql.jdbc.StringUtils;


/**
 * 邮件发送工具实现类
 * 
 */
public class MailSendUtil {

	protected static final Logger logger = Logger.getLogger(MailSendUtil.class);
	
	private static MailDto mail = new MailDto();
	
	static {
		mail.setHost(MailPropertiesUtils.getParam("host"));
		mail.setSender(MailPropertiesUtils.getParam("sender"));
		mail.setName(MailPropertiesUtils.getParam("senderName"));
		mail.setUsername(MailPropertiesUtils.getParam("userName"));
		mail.setPassword(MailPropertiesUtils.getParam("password"));
		mail.setSubject(MailPropertiesUtils.getParam("subject"));
		mail.setMessage(MailPropertiesUtils.getParam("message"));
	}
	
	/**
	 * 发送邮件
	 * 
	 * */
	public static Map<String, String> sendEmail(String receiverEmail){
		Map<String, String> resultMap = new HashMap<String, String>();
		//生成验证码
		//(int) (Math.random() * 22)+5产生的是5-26的随机数
		int randomN = (int) (Math.random() * 900000 + 100000);
		mail.setReceiver(receiverEmail);
		mail.setMessage(mail.getMessage() + randomN);
		resultMap.put("smsCode", String.valueOf(randomN));
		if(send(mail)){
			resultMap.put("code", "0000");
		}else{
			resultMap.put("code", "1111");
		}
		return resultMap;
	}
	
	/**
	 * 發送郵件方法
	 * 
	 * */
	private static boolean send(MailDto mail) {
		// 发送email
		//SimpleEmail email = new SimpleEmail();
		HtmlEmail email = new HtmlEmail();
		try {
			email.setHostName(mail.getHost());
			email.setSSLOnConnect(true); 
			// 字符编码集的设置
			email.setCharset(MailDto.ENCODEING);
			// 收件人的邮箱
			email.addTo(mail.getReceiver());
			// 发送人的邮箱
			email.setFrom(mail.getSender(), mail.getName());
			// 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
			email.setAuthentication(mail.getUsername(), mail.getPassword());
			// 要发送的邮件主题
			email.setSubject(mail.getSubject());
			// 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
			email.setMsg(mail.getMessage());
			// 发送
			email.send();
			logger.info(mail.getSender() + " 发送邮件到 " + mail.getReceiver() + " 成功");
		} catch (EmailException e) {
			e.printStackTrace();
			logger.info(mail.getSender() + " 发送邮件到 " + mail.getReceiver() + " 失败");
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		//(int) (Math.random() * 22)+5产生的是5-26的随机数
		//100000-999999
		int randomN = (int) (Math.random() * 900000 + 100000);
		System.out.println(randomN);
		
	}

}