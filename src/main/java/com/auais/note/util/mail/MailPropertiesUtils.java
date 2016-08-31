package com.auais.note.util.mail;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.io.IOException;
  
/**
* ��ȡProperties�ļ�������
* File: mail.properties
*/ 
public final class MailPropertiesUtils {
	
	private static Properties prop = new Properties();
	
 	static{
		InputStream in = MailPropertiesUtils.class.getResourceAsStream("/mail.properties");
		try{
			prop.load(in);
		}catch(IOException e){
			e.printStackTrace();
		}
 	}
  
	/**
	* ˽�й��췽��������Ҫ��������
	*/
	private  MailPropertiesUtils() {
	}
  
	public static String getParam(String name) {
		return StringUtils.trim(prop.getProperty(name));
	}
  
}