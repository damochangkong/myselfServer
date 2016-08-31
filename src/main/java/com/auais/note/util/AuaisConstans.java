package com.auais.note.util;

public class AuaisConstans {

	//0:文本，1：图片集，2：音频
	public static final byte SECTION_TYPE_TEXT = 0;
	public static final byte SECTION_TYPE_IMAGE = 1;
	public static final byte SECTION_TYPE_AUDIO = 2;
	
	//0:文本，1：图片，2：音频
	public static final byte IMAGE_TAG_TYPE_TEXT = 0;
	public static final byte IMAGE_TAG_TYPE_IMAGE = 1;
	public static final byte IMAGE_TAG_TYPE_AUDIO = 2;

	//0:文本，1：图片，2：音频
	public static final byte AUDIO_TAG_TYPE_TEXT = 0;
	public static final byte AUDIO_TAG_TYPE_IMAGE = 1;
	
	//上传下载文件中用到的常量
	// 下载链接有效时长
	public static final int DOWNLOAD_DURATION = 3600;
	// 上传token的有效时长
	public static final int UPLOAD_DURATION = 3600;
	// 带回调函数的上传接口的回调url
	public static final String CALLBACK_URL = "http://58.221.61.66/spiderNote/connect/uploadWithCallBack/getToken";
	// 带回调函数的上传接口的回调body
	public static final String CALLBACK_BODY = "filename=$(fname)&filesize=$(fsize)";
	
	public static final String RETURN_BODY = "{'key': $(key), 'hash': $(etag), 'size': $(fsize)}";
	
	public static final long INIT_USERID = 10000;
	
}
