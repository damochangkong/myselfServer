package com.auais.note.util.mail;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import com.mysql.jdbc.StringUtils;


/**
 * �ʼ����͹���ʵ����
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
	 * �����ʼ�
	 * 
	 * */
	public static Map<String, String> sendEmail(String receiverEmail){
		Map<String, String> resultMap = new HashMap<String, String>();
		//������֤��
		//(int) (Math.random() * 22)+5��������5-26�������
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
	 * �l���]������
	 * 
	 * */
	private static boolean send(MailDto mail) {
		// ����email
		//SimpleEmail email = new SimpleEmail();
		HtmlEmail email = new HtmlEmail();
		try {
			email.setHostName(mail.getHost());
			email.setSSLOnConnect(true); 
			// �ַ����뼯������
			email.setCharset(MailDto.ENCODEING);
			// �ռ��˵�����
			email.addTo(mail.getReceiver());
			// �����˵�����
			email.setFrom(mail.getSender(), mail.getName());
			// �����Ҫ��֤��Ϣ�Ļ���������֤���û���-���롣�ֱ�Ϊ���������ʼ��������ϵ�ע�����ƺ�����
			email.setAuthentication(mail.getUsername(), mail.getPassword());
			// Ҫ���͵��ʼ�����
			email.setSubject(mail.getSubject());
			// Ҫ���͵���Ϣ������ʹ����HtmlEmail���������ʼ�������ʹ��HTML��ǩ
			email.setMsg(mail.getMessage());
			// ����
			email.send();
			logger.info(mail.getSender() + " �����ʼ��� " + mail.getReceiver() + " �ɹ�");
		} catch (EmailException e) {
			e.printStackTrace();
			logger.info(mail.getSender() + " �����ʼ��� " + mail.getReceiver() + " ʧ��");
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		//(int) (Math.random() * 22)+5��������5-26�������
		//100000-999999
		int randomN = (int) (Math.random() * 900000 + 100000);
		System.out.println(randomN);
		
	}

}