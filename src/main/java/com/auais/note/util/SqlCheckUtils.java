package com.auais.note.util;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

public class SqlCheckUtils {
	
	/**
	 * ÊÇ·ñÓĞsql×¢Èë
	 * 
	 * */
	public static boolean sqlInject(String str)
	{
		String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";
		String[] inj_stra = StringUtils.split(inj_str,"|");
		if(Arrays.binarySearch(inj_stra, inj_str)>0){
			return true;
		}
		return false;
	}

}
