package com.auais.note.util.mail;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.io.IOException;
  
/**
* 读取Properties文件的例子
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
	* 私有构造方法，不需要创建对象
	*/
	private  MailPropertiesUtils() {
	}
  
	public static String getParam(String name) {
		return StringUtils.trim(prop.getProperty(name));
	}
  
}