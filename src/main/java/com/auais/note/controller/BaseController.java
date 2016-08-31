package com.auais.note.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BaseController {

	static Logger log = LoggerFactory.getLogger(BaseController.class);
	public static final String VIEW = "view";
	public static final String LIST = "list";
	public static final String STATUS = "status";
	public static final String WARN = "warn";
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String MESSAGE = "message";
	public static final String MESSAGES = "messages";
	public static final String CONTENT = "content";
	public static Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

	/**
	 * AJAX���������null
	 * 
	 * @param content
	 * @param type
	 * @return
	 */
	public String ajax(HttpServletResponse response, String content, String type) {
		try {
			response.setContentType(type + ";charset=UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.getWriter().write(content);
			response.getWriter().flush();
		} catch (IOException e) {
			log.error("IOException:", e);
		}
		return null;
	}

	/**
	 * AJAX����ı�������null
	 * 
	 * @param text
	 * @return
	 */
	public String ajaxText(HttpServletResponse response, String text) {
		return ajax(response, text, "text/plain");
	}

	/**
	 * AJAX���HTML������null
	 * 
	 * @param html
	 * @return
	 */
	public String ajaxHtml(HttpServletResponse response, String html) {
		return ajax(response, html, "textml");
	}

	/**
	 * AJAX���XML������null
	 * 
	 * @param xml
	 * @return
	 */
	public String ajaxXml(HttpServletResponse response, String xml) {
		return ajax(response, xml, "text/xml");
	}

	/**
	 * �����ַ������JSON������null
	 * 
	 * @param jsonString
	 * @return
	 */
	public String ajaxJson(HttpServletResponse response, String jsonString) {
		return ajax(response, jsonString, "textml");
	}

	/**
	 * ����Map���JSON������null
	 * 
	 * @param jsonMap
	 * @return
	 */
	public String ajaxJson(HttpServletResponse response, Map<String, String> jsonMap) {
		return ajax(response, gson.toJson(jsonMap), "textml");
	}

	/**
	 * ���JSON������Ϣ������null
	 * 
	 * @param message
	 * @return
	 */
	public String ajaxJsonWarnMessage(HttpServletResponse response, String message) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, WARN);
		jsonMap.put(MESSAGE, message);
		return ajax(response, gson.toJson(jsonMap), "textml");
	}

	/**
	 * ���JSON������Ϣ������null
	 * 
	 * @param messages
	 * @return
	 */
	public String ajaxJsonWarnMessages(HttpServletResponse response, List<String> messages) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put(STATUS, WARN);
		jsonMap.put(MESSAGES, messages);
		return ajax(response, gson.toJson(jsonMap), "textml");
	}

	/**
	 * ���JSON�ɹ���Ϣ������null
	 * 
	 * @param message
	 * @return
	 */
	public String ajaxJsonSuccessMessage(HttpServletResponse response, String message) {

		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, SUCCESS);
		jsonMap.put(MESSAGE, message);
		return ajax(response, gson.toJson(jsonMap), "textml");
	}

	/**
	 * ���JSON�ɹ���Ϣ������null
	 * 
	 * @param messages
	 * @return
	 */
	public String ajaxJsonSuccessMessages(HttpServletResponse response, List<String> messages) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put(STATUS, SUCCESS);
		jsonMap.put(MESSAGES, messages);
		return ajax(response, gson.toJson(jsonMap), "textml");
	}

	/**
	 * ���JSON������Ϣ������null
	 * 
	 * @param message
	 * @return
	 */
	public String ajaxJsonErrorMessage(HttpServletResponse response, String message) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, ERROR);
		jsonMap.put(MESSAGE, message);
		return ajax(response, gson.toJson(jsonMap), "textml");
	}

	/**
	 * ���JSON������Ϣ������null
	 * 
	 * @param messages
	 * @return
	 */
	public String ajaxJsonErrorMessages(HttpServletResponse response, List<String> messages) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put(STATUS, ERROR);
		jsonMap.put(MESSAGES, messages);
		return ajax(response, gson.toJson(jsonMap), "textml");
	}

	/**
	 * ����ҳ�治����
	 */
	public void setResponseNoCache(HttpServletResponse response) {
		response.setHeader("progma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
	}
}
