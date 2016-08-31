package com.auais.note.util;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * ����MD5���ܽ���
 */
public class SHAUtil {

	public static final String KEY_SHA = "SHA";   

    public static String getResult(String inputStr)
    {
        BigInteger sha =null;
        System.out.println("=======����ǰ������:"+inputStr);
        byte[] inputData = inputStr.getBytes();   
        try {
             MessageDigest messageDigest = MessageDigest.getInstance(KEY_SHA);  
             messageDigest.update(inputData);
             sha = new BigInteger(messageDigest.digest());   
             System.out.println("SHA���ܺ�:" + sha.toString(32));   
        } catch (Exception e) {e.printStackTrace();}
        return sha.toString(32);
    }
    
    public static void main(String args[])
    {
        try {
             String inputStr = "ssss";   
             getResult(inputStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
}
