package com.auais.note.util;

import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class TokenUtil {

	private static final int INTERVAL = 10;// token����ʱ���� ��
	private static final String YAN = "auaisSpiderNoteByzdh";// ����
	private static final int HOUR = 3;// ���token�����߳�ִ��ʱ�� ʱ
	private static Logger logger = Logger.getLogger("TokenUtil");
	private static Map<Long, Token> tokenMap = new HashMap<Long, Token>();
	static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	static {
		logger.info("\n===============����TokenUtil��̬�����==================");
		listenTask();
	}

	public static Map<Long, Token> getTokenMap() {
		return tokenMap;
	}

	/**
	 * ����һ��token
	 */
	public static Token generateToken(String uniq, long id) {
		Token token = new Token(MD5(System.currentTimeMillis() + YAN + uniq + id), System.currentTimeMillis());
		synchronized (tokenMap) {
			tokenMap.put(id, token);
		}
		return token;
	}

	/**
	 * @Title: removeToken
	 * @Description: ȥ��token
	 * @param @param
	 *            nonce
	 * @param @return
	 *            ����
	 * @return boolean ��������
	 */
	public static boolean removeToken(long id) {
		synchronized (tokenMap) {
			tokenMap.remove(id);
			logger.info(tokenMap.get(id) == null ? "\n=========��ע��========" : "\n++++++++ע��ʧ��+++++++++++++++");
		}
		return true;
	}

	/**
	 * @Title: volidateToken
	 * @Description: У��token
	 * @param @param
	 *            signature
	 * @param @param
	 *            nonce
	 * @param @return
	 *            ����
	 * @return boolean ��������
	 */
	public static boolean validateToken(String signature, long id) {
		boolean flag = false;
		Token token = (Token) tokenMap.get(id);
		if (token != null && token.getSignature().equals(signature)) {
			logger.info("\n=====������=======");
			flag = true;
		}
		return flag;
	}

	/**
	 * @Title: listenTask
	 * @Description: ��ʱִ��token�����������
	 * @param ����
	 * @return void ��������
	 */
	public static void listenTask() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		// ����ÿ���HOUR�㣬�����쿪ʼ
		calendar.set(year, month, day + 1, HOUR, 0, 0);
		Date date = calendar.getTime();
		scheduler.scheduleAtFixedRate(
			new ListenToken(), 
			(date.getTime() - System.currentTimeMillis()) / 1000,
			60 * 60 * 24, TimeUnit.SECONDS);
		logger.info("\n===============���token�΄��ѽ�����==================");
	}

	/**
	 * @ClassName: ListenToken
	 * @Description: ����token�����߳�runnableʵ��
	 * @author 
	 * 
	 */
	static class ListenToken implements Runnable {
		public ListenToken() {
			super();
		}
		public void run() {
			logger.info("\n**************************ִ�м���token�б�****************************");
			try {
				synchronized (tokenMap) {
					for (int i = 0; i < 5; i++) {
						if (tokenMap != null && !tokenMap.isEmpty()) {
							for (Entry<Long, Token> entry : tokenMap.entrySet()) {
								Token token = (Token) entry.getValue();
								logger.info("\n==============�ѵ�¼�û��У�" + entry + "=====================");
								int interval = (int) (
										(System.currentTimeMillis() - token.getTimestamp()) / 1000 / 60/ 60 / 24);
								if (interval > INTERVAL) {
									tokenMap.remove(entry.getKey());
									logger.info("\n==============�Ƴ�token��" + entry + "=====================");
								}
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error("token�����̴߳���" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @Title: MD5
	 * @Description: ����
	 * @param @param
	 *            s
	 * @param @return
	 *            ����
	 * @return String ��������
	 */
	private final static String MD5(String s) {
		try {
			byte[] btInput = s.getBytes();
			// ���MD5ժҪ�㷨�� MessageDigest ����
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// ʹ��ָ�����ֽڸ���ժҪ
			mdInst.update(btInput);
			// �������
			return byte2hex(mdInst.digest());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ���ֽ�����ת����16�����ַ���
	 * 
	 * @param b
	 * @return
	 */
	private static String byte2hex(byte[] b) {
		StringBuilder sbDes = new StringBuilder();
		String tmp = null;
		for (int i = 0; i < b.length; i++) {
			tmp = (Integer.toHexString(b[i] & 0xFF));
			if (tmp.length() == 1) {
				sbDes.append("0");
			}
			sbDes.append(tmp);
		}
		return sbDes.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(generateToken("s", 1));
		System.out.println(generateToken("39076093-5F4B-41BE-82E7-27FCCB2FCFE5_1472111655784_31e824cc471359e7", 1));
		System.out.println(generateToken("s3", 1));
		System.out.println(generateToken("s4", 1));
		System.out.println(removeToken(3));
		System.out.println(getTokenMap());
	}

}