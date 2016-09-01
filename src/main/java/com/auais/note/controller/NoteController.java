package com.auais.note.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auais.note.dto.SectionDto;
import com.auais.note.dto.inter.SyncRequest;
import com.auais.note.dto.inter.SyncResponse;
import com.auais.note.dto.inter.SyncResponseProject;
import com.auais.note.dto.inter.UploadRequest;
import com.auais.note.dto.inter.UploadResponse;
import com.auais.note.dto.inter.UploadResponseNote;
import com.auais.note.dto.inter.UploadResponseSection;
import com.auais.note.pojo.Lock;
import com.auais.note.service.ImageService;
import com.auais.note.service.LockService;
import com.auais.note.service.MindService;
import com.auais.note.service.NoteService;
import com.auais.note.service.SectionService;
import com.auais.note.service.impl.SourceServiceImpl;
import com.auais.note.util.FileUtils;
import com.auais.note.util.TokenUtil;

@Controller
@RequestMapping("/note")
public class NoteController extends BaseController{

	@Resource
	private ImageService imageService;
	@Resource
	private MindService mindService;
	@Resource
	private NoteService noteService;
	@Resource
	private SectionService sectionService;
	@Resource
	private LockService lockService;
	@Resource
	private SourceServiceImpl sourceServiceImpl;
	
	/**
	 * ��Ŀͬ��
	 * 
	 * */
	@ResponseBody
	@RequestMapping(value="/syncProjects", produces = "text/html;charset=UTF-8")
	public String syncProjects(String projectInfo,@RequestParam("token") String token,
			HttpServletRequest request,HttpServletResponse response,Model model){
		response.setCharacterEncoding("UTF-8"); //���ñ����ʽ
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("------------testSync begin------------");
		System.out.println(projectInfo);
		SyncResponse syncResponse = new SyncResponse();
		syncResponse.setCode("0000");
		syncResponse.setMessage("ͬ���ɹ�");
		if(StringUtils.isEmpty(projectInfo)){
			syncResponse.setCode("1001");
			syncResponse.setMessage("����Ϊ��");
			System.out.println(gson.toJson(syncResponse));
			return gson.toJson(syncResponse);
		}
		SyncRequest syncRequest = null;
		try {
			syncRequest = gson.fromJson(projectInfo, SyncRequest.class);
		} catch (Exception e) {
			syncResponse.setCode("1002");
			syncResponse.setMessage("JSONת���쳣������JSON����ʽ");
			e.printStackTrace();
			return gson.toJson(syncResponse);
		}
		//���У��
		Map<String,String> res = this.noteService.validateSyncRequest(syncRequest);
		if(!StringUtils.equals(res.get("code"), "0000")){
			syncResponse.setCode("1003");
			syncResponse.setMessage("�������⣺"+res.get("message"));
			return gson.toJson(syncResponse);
		}
		boolean flag = TokenUtil.validateToken(token, Long.valueOf(syncRequest.getUserId()));
		if(!flag){
			syncResponse.setCode("1004");
			syncResponse.setMessage("token�Ѿ�ʧЧ�������µ�¼");
			return gson.toJson(syncResponse);
		}
		//�����������״̬��ֱ�ӷ��أ���ͻ��˵ȴ����ϴ�
		boolean isLocked = lockService.isLocked(syncRequest.getUserId(), syncRequest.getDeviceId());
		if(isLocked){
			log.info("��ʱ������״̬");
			syncResponse.setCode("1005");
			syncResponse.setMessage("��ʱ������״̬");
			return gson.toJson(syncResponse);
		}else{
			Lock lock = new Lock();
			lock.setUserId(syncRequest.getUserId());
			lock.setDeviceId(syncRequest.getDeviceId());
			lock.setLockStatus((byte)1);
			lock.setCreateAt(new Date());
			lockService.addLock(lock);
		}
		List<SectionDto> sectionList = null;
		try {
			//����Ƚ�
			Date syncTime = syncRequest.getSectionSyncTimestamp();
			sectionList = sectionService.selectListByTimeAndUserId(syncTime,syncRequest.getUserId());
			syncResponse.setSections(sectionList);
			System.out.println(gson.toJson(sectionList));
		} catch (Exception e) {
			syncResponse.setCode("1006");
			syncResponse.setMessage("��֯���䷢���쳣");
			log.info("��֯���䷢���쳣");
			lockService.deleteLock(syncRequest.getUserId(), syncRequest.getDeviceId());
			e.printStackTrace();
			return gson.toJson(syncResponse);
		}
		try {
			//��Ŀ�Ƚ�
			List<SyncResponseProject> projects = noteService.syncProjectList(syncRequest.getUserId(),
					syncRequest.getDeviceId(),syncRequest.getNotes());
			syncResponse.setNotes(projects);
			//���˵��ظ��Ķ���
			this.filterSections(sectionList, projects);
			
		} catch (Exception e) {
			syncResponse.setCode("1007");
			syncResponse.setMessage("��֯��Ŀ�����쳣");
			log.info("��֯��Ŀ�����쳣");
			lockService.deleteLock(syncRequest.getUserId(), syncRequest.getDeviceId());
			e.printStackTrace();
			return gson.toJson(syncResponse);
		}
		log.info("------------testSync end------------");
		System.out.println(gson.toJson(syncResponse));
		return gson.toJson(syncResponse);
	}
	
	@ResponseBody
	@RequestMapping(value="/uploadProjects", produces = "text/html;charset=UTF-8")
	public String uploadProjects(String projectInfo,@RequestParam("token") String token,
			HttpServletRequest request,HttpServletResponse response,Model model){
		response.setCharacterEncoding("UTF-8"); //���ñ����ʽ
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("------------uploadProjects------------");
		System.out.println(projectInfo);
		UploadResponse uploadResponse = new UploadResponse();
		uploadResponse.setCode("0000");
		uploadResponse.setMessage("ͬ���ɹ�");
		UploadRequest uploadRequest = null;
		if(StringUtils.isEmpty(projectInfo)){
			uploadResponse.setCode("1001");
			uploadResponse.setMessage("��Ŀ����Ϊ��");
			return gson.toJson(uploadResponse);
		}
		if(StringUtils.isEmpty(token)){
			uploadResponse.setCode("1002");
			uploadResponse.setMessage("token����Ϊ��");
			return gson.toJson(uploadResponse);
		}
		try {
			uploadRequest = gson.fromJson(projectInfo, UploadRequest.class);
		} catch (Exception e) {
			uploadResponse.setCode("1003");
			uploadResponse.setMessage("JSONת���쳣������JSON����ʽ");
			lockService.deleteLock(uploadRequest.getUserId(), uploadRequest.getDeviceId());
			e.printStackTrace();
			return gson.toJson(uploadResponse);
		}
		//���У��
		Map<String,String> res = this.noteService.validateUploadRequest(uploadRequest);
		if(!StringUtils.equals(res.get("code"), "0000")){
			uploadResponse.setCode("1005");
			uploadResponse.setMessage("�������⣺"+res.get("message"));
			lockService.deleteLock(uploadRequest.getUserId(), uploadRequest.getDeviceId());
			return gson.toJson(uploadResponse);
		}
		//tokenУ��
		boolean flag = TokenUtil.validateToken(token, Long.valueOf(uploadRequest.getUserId()));
		if(!flag){
			uploadResponse.setCode("1004");
			uploadResponse.setMessage("token�Ѿ�ʧЧ�������µ�¼");
			lockService.deleteLock(uploadRequest.getUserId(), uploadRequest.getDeviceId());
			return gson.toJson(uploadResponse);
		}
		//�����������״̬��ֱ�ӷ��أ���ͻ��˵ȴ����ϴ�
		boolean isLocked = lockService.hasHoldLock(uploadRequest.getUserId(), uploadRequest.getDeviceId());
		if(!isLocked){
			log.info("û�л����");
			uploadResponse.setCode("1006");
			uploadResponse.setMessage("û�л����");
			return gson.toJson(uploadResponse);
		}
		Date syncTime = new Date();
		//1.�������
		UploadResponseSection sectionInfo = new UploadResponseSection();
		try {
			List<SectionDto> sections = uploadRequest.getSections();
			this.sectionService.uploadSaveSectionList(sections, uploadRequest.getUserId(),syncTime);
			sectionInfo.setSyncTimestamp(syncTime);
		} catch (Exception e) {
			sectionInfo.setCode("1007");
			sectionInfo.setMessage("�������ʱ��������");
			uploadResponse.setCode("1001");
			uploadResponse.setMessage("�������ʱ��������");
			uploadResponse.setSection(sectionInfo);
			lockService.deleteLock(uploadRequest.getUserId(), uploadRequest.getDeviceId());
			log.info("--------����������" + e);
			e.printStackTrace();
			return gson.toJson(uploadResponse);
		}
		uploadResponse.setSection(sectionInfo);
		//2.������Ŀ
		List<UploadResponseNote> responseNotes = null;
		try {
			responseNotes = this.noteService.uploadNotes(uploadRequest.getNotes(), uploadRequest.getUserId(), syncTime);
		} catch (Exception e) {
			uploadResponse.setCode("1008");
			uploadResponse.setMessage("������Ŀʱ��������");
			log.info("--------������Ŀ����" + e);
			e.printStackTrace();
			lockService.deleteLock(uploadRequest.getUserId(), uploadRequest.getDeviceId());
			return gson.toJson(uploadResponse);
		}
		uploadResponse.setNotes(responseNotes);
		System.out.println(gson.toJson(uploadResponse));
		//3.����������
		try {
			this.sourceServiceImpl.uploadUpdateSourceAfterUpload(uploadRequest.getUserId());
		} catch (Exception e) {
			uploadResponse.setCode("1009");
			uploadResponse.setMessage("�������������");
			log.info("--------�������������" + e);
			e.printStackTrace();
			lockService.deleteLock(uploadRequest.getUserId(), uploadRequest.getDeviceId());
			return gson.toJson(uploadResponse);
		}
		System.out.println(gson.toJson(uploadResponse));
		lockService.deleteLock(uploadRequest.getUserId(), uploadRequest.getDeviceId());
		return gson.toJson(uploadResponse);
	}
	
	/**
	 * ���˵��ظ��Ķ���
	 * ����Ŀ�еĶ�������Ķ���ϲ�
	 * ����������Ŀ�еĶ���
	 * */
	private void filterSections(List<SectionDto> sectionList, List<SyncResponseProject> projects){
		List<SectionDto> _temp = null;
		for(SyncResponseProject project : projects){
			if(null!=project.getData()){
				_temp = project.getData().getSections();
				if(null!= _temp && _temp.size()>0){
					sectionList.removeAll(_temp);
				}
			}
		}
	}

	/**
	 * ��ʾ����ҳ��
	 * 
	 * */
	@RequestMapping("/showMind")
	public String showMind(HttpServletRequest request,Model model){
		return "showMind";
	}
	
	@ResponseBody
	@RequestMapping("/test")
	public String tests(String note,HttpServletRequest request,HttpServletResponse response,Model model){
		System.out.println("note1:" + note);
		note = String.valueOf(request.getAttribute("note"));
		System.out.println("note2:" + note);
		System.out.println("note3:" + request.getParameter("note"));
		return "{'flag':'success'}";
	}
	

	/**
	 * �����������
	 * 
	 * */
	@ResponseBody
	@RequestMapping(value="/uploadTest", produces = "text/html;charset=UTF-8")
	public String uploadTest(String projectInfo,String token,
			HttpServletRequest request,HttpServletResponse response,Model model){
        String fileName = "F:/mayunProject/SpiderNoteServer/document/jsonData/interfaceJson/projectUploadIn.json";
        projectInfo = FileUtils.readFileByLines(fileName);
        return this.uploadProjects(projectInfo, token, request, response, model);
	}
	
	/**
	 * ��������������
	 * 
	 * **/
	@ResponseBody
	@RequestMapping(value="/organizeSource", produces = "text/html;charset=UTF-8")
	public String organizeSource(
			HttpServletRequest request,HttpServletResponse response,Model model){
		this.sourceServiceImpl.uploadUpdateSourceAfterUpload("user0001");
		return "{'flag':'success'}";
	}
	
	
}
