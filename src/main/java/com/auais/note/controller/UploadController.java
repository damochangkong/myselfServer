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
	// 创建上传对象
	UploadManager uploadManager = new UploadManager();

	Auth auth = null;

	@Resource
	private UploadServiceImpl uploadServiceImpl;

	/**
	 * 初始化出管理者
	 * 
	 */
	@InitBinder
	void initAction() {
		auth = Auth.create(propertyContain.getAccessKey(), propertyContain.getSecretKey());
	}

	/**
	 * 简单上传，使用默认策略，只需要设置上传的空间名就可以了
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
	 * 覆盖上传 <bucket>:<key>，表示只允许用户上传指定key的文件。
	 * 在这种格式下文件默认允许“修改”，已存在同名资源则会被本次覆盖。
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
	 * 设置callbackUrl以及callbackBody 七牛将文件名和文件大小回调给业务服务器
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
	 * 上传图片之后的回调函数
	 * 
	 */
	@RequestMapping("/uploadCallback")
	public void callbackAction(@RequestParam("filename") String filename, 
			@RequestParam("filesize") String filesize,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		uploadServiceImpl.refreshImageSize(filename, filesize);
	}

	/**
	 * 生成一個下載鏈接
	 * 
	 */
	@RequestMapping("/download/getDownloadUrl")
	public void getDownloadUrl(@RequestParam("key") String key, 
			HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String url = propertyContain.getDownloadUrl();
		StringBuffer sb = new StringBuffer();
		url = sb.append(true).append(key).toString();
		// 调用privateDownloadUrl方法生成下载链接,第二个参数可以设置Token的过期时间
		String downloadRUL = auth.privateDownloadUrl(url, AuaisConstans.DOWNLOAD_DURATION);
		System.out.println(downloadRUL);
	}

	/**
	 * java上传测试
	 * 
	 */
	@RequestMapping("/upload")
	public void upload(String key, String filePath, String token) throws IOException {
		try {
			// 调用put方法上传
			Response res = uploadManager.put(filePath, key, token);
			// 打印返回的信息
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
			// 请求失败时打印的异常的信息
			System.out.println(r.toString());
			try {
				// 响应的文本信息
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
		// 设置需要操作账号的AK和SK
		String ACCESS_KEY = "1f5Ww9kn1p7QXzJqtPIATXkvZuLejPTMKWnwSRwV";
		String SECRET_KEY = "9C6nXc6PnfeubyxhAs-3Ep_HBGoMNOwWo8M4dTj2";
		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		// 实例化一个BucketManager对象
		BucketManager bucketManager = new BucketManager(auth);
		// 要列举文件的空间名
		String bucket = "auaisspider";
		try {
			// 调用listFiles方法列举指定空间的指定文件
			// 参数一：bucket 空间名
			// 参数二：prefix 文件名前缀
			// 参数三：marker 上一次获取文件列表时返回的marker
			// 参数四：limit 每次迭代的长度限制，最大1000，推荐值100
			// 参数五：delimiter 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
			FileListing fileListing = bucketManager.listFiles(bucket, "dafeng", null, 1000, null);
			FileInfo[] items = fileListing.items;
			for (FileInfo fileInfo : items) {
				System.out.println(fileInfo.key + "," + fileInfo.fsize + "," + fileInfo.endUser);
			}
		} catch (QiniuException e) {
			// 捕获异常信息
			Response r = e.response;
			System.out.println(r.toString());
		}
	}
}
