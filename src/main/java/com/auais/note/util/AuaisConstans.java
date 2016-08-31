package com.auais.note.util;

public class AuaisConstans {

	//0:�ı���1��ͼƬ����2����Ƶ
	public static final byte SECTION_TYPE_TEXT = 0;
	public static final byte SECTION_TYPE_IMAGE = 1;
	public static final byte SECTION_TYPE_AUDIO = 2;
	
	//0:�ı���1��ͼƬ��2����Ƶ
	public static final byte IMAGE_TAG_TYPE_TEXT = 0;
	public static final byte IMAGE_TAG_TYPE_IMAGE = 1;
	public static final byte IMAGE_TAG_TYPE_AUDIO = 2;

	//0:�ı���1��ͼƬ��2����Ƶ
	public static final byte AUDIO_TAG_TYPE_TEXT = 0;
	public static final byte AUDIO_TAG_TYPE_IMAGE = 1;
	
	//�ϴ������ļ����õ��ĳ���
	// ����������Чʱ��
	public static final int DOWNLOAD_DURATION = 3600;
	// �ϴ�token����Чʱ��
	public static final int UPLOAD_DURATION = 3600;
	// ���ص��������ϴ��ӿڵĻص�url
	public static final String CALLBACK_URL = "http://58.221.61.66/spiderNote/connect/uploadWithCallBack/getToken";
	// ���ص��������ϴ��ӿڵĻص�body
	public static final String CALLBACK_BODY = "filename=$(fname)&filesize=$(fsize)";
	
	public static final String RETURN_BODY = "{'key': $(key), 'hash': $(etag), 'size': $(fsize)}";
	
	public static final long INIT_USERID = 10000;
	
}
