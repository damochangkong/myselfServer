package com.auais.note.controller;

import java.io.IOException;
import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auais.note.dto.UploadResDto;
import com.auais.note.service.impl.UploadServiceImpl;
import com.auais.note.util.AuaisConstans;
import com.auais.note.util.PropertyContain;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

@Controller
@RequestMapping("/connect")
public class UploadController extends BaseController {
	@Resource
	private PropertyContain propertyContain;
	// �����ϴ�����
	UploadManager uploadManager = new UploadManager();

	Auth auth = null;

	@Resource
	private UploadServiceImpl uploadServiceImpl;

	/**
	 * ��ʼ����������
	 * 
	 */
	@InitBinder
	void initAction() {
		auth = Auth.create(propertyContain.getAccessKey(), propertyContain.getSecretKey());
	}

	/**
	 * ���ϴ���ʹ��Ĭ�ϲ��ԣ�ֻ��Ҫ�����ϴ��Ŀռ����Ϳ�����
	 * 
	 */
	@ResponseBody
	@RequestMapping("/upload/getToken")
	public String getUpToken(
			@RequestParam("buckerName") String buckerName, 
			HttpServletRequest request,HttpServletResponse response, Model model) {
		return auth.uploadToken(buckerName);
	}

	/**
	 * �����ϴ� <bucket>:<key>����ʾֻ�����û��ϴ�ָ��key���ļ���
	 * �����ָ�ʽ���ļ�Ĭ�������޸ġ����Ѵ���ͬ����Դ��ᱻ���θ��ǡ�
	 * 
	 */
	@ResponseBody
	@RequestMapping("/uploadOverride/getToken")
	public String getOverrideUpToken(
			@RequestParam("buckerName") String buckerName, 
			@RequestParam("key") String key,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		return auth.uploadToken(buckerName, key);
	}

	/**
	 * ����callbackUrl�Լ�callbackBody ��ţ���ļ������ļ���С�ص���ҵ�������
	 * 
	 */
	@ResponseBody
	@RequestMapping("/uploadWithCallBack/getToken")
	public String getCallBackUpToken(
			@RequestParam("buckerName") String buckerName, 
			@RequestParam("key") String key,
			HttpServletRequest request,HttpServletResponse response, Model model) {
		StringMap map = new StringMap();
		map.put("callbackUrl", AuaisConstans.CALLBACK_URL);
		map.put("callbackBody", AuaisConstans.CALLBACK_BODY);
		return auth.uploadToken(buckerName, key, AuaisConstans.UPLOAD_DURATION, map);
	}

	/**
	 * �ϴ�ͼƬ֮��Ļص�����
	 * 
	 */
	@RequestMapping("/uploadCallback")
	public void callbackAction(@RequestParam("filename") String filename, 
			@RequestParam("filesize") String filesize,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		uploadServiceImpl.refreshImageSize(filename, filesize);
	}

	/**
	 * ����һ�����d朽�
	 * 
	 */
	@RequestMapping("/download/getDownloadUrl")
	public void getDownloadUrl(@RequestParam("key") String key, 
			HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String url = propertyContain.getDownloadUrl();
		StringBuffer sb = new StringBuffer();
		url = sb.append(true).append(key).toString();
		// ����privateDownloadUrl����������������,�ڶ���������������Token�Ĺ���ʱ��
		String downloadRUL = auth.privateDownloadUrl(url, AuaisConstans.DOWNLOAD_DURATION);
		System.out.println(downloadRUL);
	}

	/**
	 * java�ϴ�����
	 * 
	 */
	@RequestMapping("/upload")
	public void upload(String key, String filePath, String token) throws IOException {
		try {
			// ����put�����ϴ�
			Response res = uploadManager.put(filePath, key, token);
			// ��ӡ���ص���Ϣ
			String responseStr = res.bodyString();
			System.out.println(responseStr);
			UploadResDto uploadResDto = gson.fromJson(responseStr, UploadResDto.class);
			long size = Long.valueOf(uploadResDto.getSize());
			double sizeD = size / (1024 * 1024);
			BigDecimal sizeB = new BigDecimal(sizeD);
			System.out.println("sizeB:" + sizeB);
			sizeD = sizeB.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
			System.out.println(sizeD);
		} catch (QiniuException e) {
			e.printStackTrace();
			Response r = e.response;
			// ����ʧ��ʱ��ӡ���쳣����Ϣ
			System.out.println(r.toString());
			try {
				// ��Ӧ���ı���Ϣ
				System.out.println(r.bodyString());
			} catch (QiniuException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void main33(String[] args) throws IOException {
		 
		 String bucketName = "auaisspider";
		 String key = "dafeng/langjitianya.png";
		 UploadController uploadController = new UploadController();
		 Auth auth = Auth.create("1f5Ww9kn1p7QXzJqtPIATXkvZuLejPTMKWnwSRwV",
		 "9C6nXc6PnfeubyxhAs-3Ep_HBGoMNOwWo8M4dTj2");
		 String token = auth.uploadToken(bucketName, key);
		 StringMap map = new StringMap();
		 map.put("returnBody", "{'key': $(key), 'hash': $(etag), 'size':$(fsize)}");
		 map.put("endUser", "zhudahong");
		 token = auth.uploadToken(bucketName,null,AuaisConstans.UPLOAD_DURATION,map);
		 System.out.println(token);
		 String filePath = "F:/mayunProject/SpiderNoteServer/document/testPic/background.png";
		 uploadController.upload(key, filePath, token);
		 
	}

	public static void main(String args[]) {
		// ������Ҫ�����˺ŵ�AK��SK
		String ACCESS_KEY = "1f5Ww9kn1p7QXzJqtPIATXkvZuLejPTMKWnwSRwV";
		String SECRET_KEY = "9C6nXc6PnfeubyxhAs-3Ep_HBGoMNOwWo8M4dTj2";
		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		// ʵ����һ��BucketManager����
		BucketManager bucketManager = new BucketManager(auth);
		// Ҫ�о��ļ��Ŀռ���
		String bucket = "auaisspider";
		try {
			// ����listFiles�����о�ָ���ռ��ָ���ļ�
			// ����һ��bucket �ռ���
			// ��������prefix �ļ���ǰ׺
			// ��������marker ��һ�λ�ȡ�ļ��б�ʱ���ص�marker
			// �����ģ�limit ÿ�ε����ĳ������ƣ����1000���Ƽ�ֵ100
			// �����壺delimiter ָ��Ŀ¼�ָ������г����й���ǰ׺��ģ���г�Ŀ¼Ч������ȱʡֵΪ���ַ���
			FileListing fileListing = bucketManager.listFiles(bucket, "dafeng", null, 1000, null);
			FileInfo[] items = fileListing.items;
			for (FileInfo fileInfo : items) {
				System.out.println(fileInfo.key + "," + fileInfo.fsize + "," + fileInfo.endUser);
			}
		} catch (QiniuException e) {
			// �����쳣��Ϣ
			Response r = e.response;
			System.out.println(r.toString());
		}
	}
}
