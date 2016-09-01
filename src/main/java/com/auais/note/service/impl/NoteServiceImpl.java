package com.auais.note.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.auais.note.controller.BaseController;
import com.auais.note.dao.MindMapper;
import com.auais.note.dao.NoteMapper;
import com.auais.note.dto.MindDto;
import com.auais.note.dto.SectionDto;
import com.auais.note.dto.inter.CommonFullNote;
import com.auais.note.dto.inter.SyncRequest;
import com.auais.note.dto.inter.SyncRequsetNote;
import com.auais.note.dto.inter.SyncResponseProject;
import com.auais.note.dto.inter.UploadRequest;
import com.auais.note.dto.inter.UploadResponseNote;
import com.auais.note.pojo.Note;
import com.auais.note.service.MindService;
import com.auais.note.service.NoteService;
import com.auais.note.service.SectionService;
import com.auais.note.util.AuaisConstans;
import com.auais.note.util.DateUtils;

@Service("noteService")
public class NoteServiceImpl implements NoteService{

	static Logger logger = LoggerFactory.getLogger(BaseController.class);
	@Resource
	public NoteMapper noteMapper;
	@Resource
	public MindMapper mindMapper;
	@Resource
	public MindService mindService;
	@Resource
	public SectionService sectionService;
	
	@Override
	public List<SyncResponseProject> syncProjectList(String userId,String deviceId,List<SyncRequsetNote> syncRequsetNoteList){
		
		//锁定对象？拿不到锁直接返回，拿到锁继续（插入一条锁记录）
		
		//判断一下服务端是否有新的项目，客户端没有的，全部返回给客户端
		List<Note> _notes = this.noteMapper.selectNoteListByUserId(userId);
		List<SyncRequsetNote> _syncNotes = new ArrayList<SyncRequsetNote>();
		for(Note _note : _notes){
			_syncNotes.add(_note.convertToSyncRequsetNote());
		}
		if(null==syncRequsetNoteList || syncRequsetNoteList.size()==0){
			syncRequsetNoteList= new ArrayList<SyncRequsetNote>();
		}
		for(SyncRequsetNote _note : _syncNotes){
			if(!syncRequsetNoteList.contains(_note)){
				_note.setModifyFlag((byte)2);
				syncRequsetNoteList.add(_note);
			}
		}
		List<SyncResponseProject> projects = new ArrayList<SyncResponseProject>();
		for(SyncRequsetNote _curr : syncRequsetNoteList){
			projects.add(this.syncProject(deviceId,_curr));
		}
		return projects;
	}
	
	/**
	 * 判断一个项目是否是更高的版本，或者是否有冲突等
	 * */
	public SyncResponseProject syncProject(String deviceId,SyncRequsetNote syncRequsetNote){
		SyncResponseProject project = new SyncResponseProject();
		String noteId = syncRequsetNote.getNoteId();
		Date syncTimestamp = syncRequsetNote.getSyncTimestamp();
		Byte modifyFlag = syncRequsetNote.getModifyFlag();
		Note _note = noteMapper.selectByPrimaryKey(noteId);
		project.setNoteId(noteId);
		if(null == _note){
			project.setCode("1001");
			project.setMessage("项目为新项目，请直接上传");
			return project;
		}
		if(modifyFlag ==2){
			CommonFullNote syncResponseNote = this.organizeSyncResponseNote(_note,null);
			project.setData(syncResponseNote);
			project.setCode("1004");
			project.setMessage("项目客户端不存在，服务端存在，是新项目，直接下载");
			return project;
		}
		if(StringUtils.equals(_note.getDeviceId(), deviceId)){
			project.setCode("1002");
			project.setMessage("设备ID一致，请直接上传");
			return project;
		}
		//版本号一致
		if(syncTimestamp.compareTo(_note.getSyncTimestamp())==0){
			project.setCode("1003");
			project.setMessage("版本号一致，请继续上传");
			return project;
		}
		//服务端的版本号更高
		if(syncTimestamp.compareTo(_note.getSyncTimestamp())<0){
			CommonFullNote noteDto = _note.convertToCommonFullNote();
			project.setData(noteDto);
			//本地没修改
			if(modifyFlag==0){
				//服务端没删除
				if(_note.getDeleteFlag()==0){
					List<MindDto> minds = this.mindService.getMindListBySyncTime(DateUtils.formatDateStr(syncTimestamp));
					//------修改了节点的话，返回高版本的节点
					if(null!=minds && minds.size()>0){
						project.setCode("2001");
						project.setMessage("本地无修改，服务端有更高版本，并且节点有修改，返回项目信息和高个版本的节点信息");
						noteDto.setMinds(minds);
					}else{
						project.setCode("2002");
						project.setMessage("本地无修改，服务端有更高版本，但是节点没有修改，仅仅是名字或者一节节点发生修改，仅返回项目信息");
					}
				}else{
					project.setCode("2003");
					project.setMessage("本地无修改，服务端项目已经删除，仅仅返回项目信息");
				}
			}else{
				//服务端没删除
				if(_note.getDeleteFlag()==0){
					//------返回全量项目信息
					CommonFullNote syncResponseNote = this.organizeSyncResponseNote(_note,null);
					project.setData(syncResponseNote);
					project.setCode("3001");
					project.setMessage("本地已经修改，服务端项目没有删除，返回全量项目信息");
				}else{
					project.setCode("3002");
					project.setMessage("本地已经修改，但是服务端项目已经删除，仅返回项目信息");
				}
			}
		}
		return project;
	}
	
	/**
	 * 组织出节点列表和段落列表
	 * */
	public CommonFullNote organizeSyncResponseNote(Note _note,CommonFullNote syncResponseNote) {
		if(null == syncResponseNote){
			syncResponseNote = _note.convertToCommonFullNote();
		}
		//根据noteId组织出节点列表
		List<MindDto> minds = this.mindService.getMindListByNoteId(_note.getId());
		syncResponseNote.setMinds(minds);
		//根据noteId组织出段落列表
		List<SectionDto> sections = sectionService.selectListByNoteId(_note.getId());
		syncResponseNote.setSections(sections);
		return syncResponseNote;
	}
	
	/**
	 * 简介调用上面的方法
	 * */
	public CommonFullNote organizeNoteDtoById(String noteId) {
		Note _note = noteMapper.selectByPrimaryKey(noteId);
		return this.organizeSyncResponseNote(_note,null);
	}
	
	/**
	 * 上传接口的总入口
	 * */
	public List<UploadResponseNote> uploadNotes(List<CommonFullNote> notes,String userId,Date syncTime){
		logger.info("--------uploadNotes---------");
		if(null==notes || notes.size()==0){
			return null;
		}
		List<UploadResponseNote> noteInfo = new ArrayList<UploadResponseNote>();
		UploadResponseNote _temp = null;
		for(CommonFullNote _curr : notes){
			_temp = this.uploadSingleNote(_curr,userId,syncTime);
			_temp.setSyncTimestamp(syncTime);
			noteInfo.add(_temp);
		}
		return noteInfo;
	}
	
	private UploadResponseNote uploadSingleNote(CommonFullNote reqNote,String userId,Date syncTime){
		UploadResponseNote resNote = new UploadResponseNote();
		resNote.setCode("0000");
		//保存段落
		try {
			this.sectionService.uploadSaveSectionList(reqNote.getSections(), userId, syncTime);
		} catch (Exception e) {
			resNote.setCode("1001");
			resNote.setMessage("保存段落发生异常");
			logger.info("--------保存段落异常：" + e);
			e.printStackTrace();
			return resNote;
		}
		//保存节点
		try {
			this.mindService.saveMindList(reqNote.getMinds(), reqNote.getId(),syncTime);
		} catch (Exception e) {
			resNote.setCode("1002");
			resNote.setMessage("保存节点发生异常");
			logger.info("--------保存节点异常：" + e);
			e.printStackTrace();
			return resNote;
		}
		//保存项目信息
		try {
			this.saveNote(reqNote, userId, syncTime);
		} catch (Exception e) {
			resNote.setCode("1003");
			resNote.setMessage("保存项目本身的发生异常");
			logger.info("--------保存项目本身的异常：" + e);
			e.printStackTrace();
			return resNote;
		}
		return resNote;
	}
	
	/**
	 * 批量处理项目信息
	 * */
	private int saveNote(CommonFullNote reqNote,String userId,Date syncTime){
		int res;
		Note _note = reqNote.convertToNote();
		_note.setSyncTimestamp(syncTime);
		if(null!=this.noteMapper.selectByPrimaryKey(reqNote.getId())){
			res = this.noteMapper.updateByPrimaryKey(_note);
			//如果是项目删除，则清除项目下的段落所有的资源
			if(null!=reqNote.getDeleteFlag() && reqNote.getDeleteFlag()==1){
				this.sectionService.clearSectionAndSource(reqNote);
			}
		}else{
			res = this.noteMapper.insert(_note);
		}
		return res;
	}

	/**
	 * 同步接口入参校验
	 * */
	public Map<String,String> validateSyncRequest(SyncRequest syncRequest){
		Map<String,String> res = new HashMap<String,String>();
		res.put("code", "0000");
		if(StringUtils.isEmpty(syncRequest.getUserId())){
			res.put("code", "1001");
			res.put("message", "用户ID为空");
			return res;
		}
		if(!NumberUtils.isDigits(syncRequest.getUserId())){
			res.put("code", "1002");
			res.put("message", "用户ID非法");
			return res;
		}
		if(StringUtils.isEmpty(syncRequest.getDeviceId())){
			res.put("code", "1003");
			res.put("message", "设备ID为空");
			return res;
		}
		List<SyncRequsetNote> notes = syncRequest.getNotes();
		for(SyncRequsetNote note : notes){
			if(StringUtils.isEmpty(note.getNoteId())){
				res.put("code", "1004");
				res.put("message", "项目ID为空");
				return res;
			}
		}
		return res;
	}
	
	/**
	 * 校验上传接口的数据
	 * */
	@Override
	public Map<String,String> validateUploadRequest(UploadRequest uploadRequest){
		Map<String,String> res = new HashMap<String,String>();
		res.put("code", "0000");
		if(StringUtils.isEmpty(uploadRequest.getUserId())){
			res.put("code", "1001");
			res.put("message", "用户ID为空");
		}
		if(!NumberUtils.isDigits(uploadRequest.getUserId())){
			res.put("code", "1002");
			res.put("message", "用户ID非法");
			return res;
		}
		if(StringUtils.isEmpty(uploadRequest.getDeviceId())){
			res.put("code", "1003");
			res.put("message", "设备ID为空");
		}
		List<CommonFullNote> notes = uploadRequest.getNotes();
		if(null!=notes && notes.size()>0){
			for(CommonFullNote note : notes){
				res = this.validateCommonFullNote(note);
				if(!StringUtils.equals(res.get("code"), "0000")){
					return res;
				}
			}
		}
		List<SectionDto> sections = uploadRequest.getSections();
		if(null!=sections && sections.size()>0){
			for(SectionDto section : sections){
				res = this.validateSectionDto(section);
				if(!StringUtils.equals(res.get("code"), "0000")){
					return res;
				}
			}
		}
		return res;
	}
	
	/**
	 * 校验上传接口中项目的数据
	 * */
	private Map<String,String> validateCommonFullNote(CommonFullNote note){
		Map<String,String> res = new HashMap<String,String>();
		res.put("code", "0000");
		if(StringUtils.isEmpty(note.getId())){
			res.put("code", "2001");
			res.put("message", "项目ID为空");
			return res;
		}
		if(StringUtils.isEmpty(note.getUserId())){
			res.put("code", "2002");
			res.put("message", "用户ID为空");
			return res;
		}
		if(StringUtils.isEmpty(note.getName())){
			res.put("code", "2003");
			res.put("message", "项目名称为空");
			return res;
		}
		if(StringUtils.isEmpty(note.getDeviceId())){
			res.put("code", "2004");
			res.put("message", "设备ID为空");
			return res;
		}
		return res;
	}
	
	/**
	 * 校验上传接口中段落的数据
	 * */
	private Map<String,String> validateSectionDto(SectionDto section){
		Map<String,String> res = new HashMap<String,String>();
		res.put("code", "0000");
		if(StringUtils.isEmpty(section.getId())){
			res.put("code", "3001");
			res.put("message", "段落ID为空");
			return res;
		}
		if(StringUtils.isEmpty(section.getUserId())){
			res.put("code", "3002");
			res.put("message", "用户ID为空");
			return res;
		}
		if(StringUtils.isEmpty(String.valueOf(section.getSectionType()))){
			res.put("code", "3003");
			res.put("message", "段落类型为空");
			return res;
		}
		if(section.getSectionType()==AuaisConstans.SECTION_TYPE_AUDIO){
			if(null == section.getAudioId() && section.getDeleteFlag()!=1){
				res.put("code", "3004");
				res.put("message", "段落为音频段落，但是音频信息为空");
				return res;
			}
		}
		if(section.getSectionType()==AuaisConstans.SECTION_TYPE_IMAGE){
			if((null == section.getImageIds() || section.getImageIds().isEmpty()) && section.getDeleteFlag()!=1){
				res.put("code", "3005");
				res.put("message", "段落为图片段落，但是图片信息为空");
				return res;
			}
		}
		if(section.getSectionType()==AuaisConstans.SECTION_TYPE_TEXT  && section.getDeleteFlag()!=1){
			if(StringUtils.isEmpty(section.getText())){
				res.put("code", "3006");
				res.put("message", "段落为文字段落，但是文字信息为空");
				return res;
			}
		}
		//如果为空设置默认值0
		if(StringUtils.isEmpty(String.valueOf(section.getUndocFlag()))){
			section.setUndocFlag((byte)0);
		}
		return res;
	}
	
	public void updateSoruce(String userId){
		
		
		
		
	}
	
	
	
	
}
