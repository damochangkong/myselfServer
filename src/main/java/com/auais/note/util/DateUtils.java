package com.auais.note.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间日期工具类
 *
 */
public class DateUtils {

    private static SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentDateStr(){
        return formatTime.format(new Date());
    }
    
    public static String formatDateStr(Date date){
        return formatTime.format(date);
    }
    
	public static String formatDate(Date date, String dateForm) {
		SimpleDateFormat sdf =  new SimpleDateFormat(dateForm); 
	    return sdf.format(date);
	}
	
	public static Date parser(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(date);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static boolean isValidDate(String str) {
		boolean convertSuccess=true;
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    format.setLenient(false);
	    try {
	        format.parse(str);
	    } catch (ParseException e) {
	         convertSuccess=false;
	    }
	    return convertSuccess;
	}
	
	public static long getDistanceTime(Date date1, Date date2) {
        long seconds = 0;
        try {
        	seconds = (date1.getTime() - date2.getTime()) / (1000);
        } catch (Exception e) {
        	return 0;
        }
        return seconds;
	}
}
