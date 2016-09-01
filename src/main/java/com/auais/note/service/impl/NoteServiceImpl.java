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
		
		//���������ò�����ֱ�ӷ��أ��õ�������������һ������¼��
		
		//�ж�һ�·�����Ƿ����µ���Ŀ���ͻ���û�еģ�ȫ�����ظ��ͻ���
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
	 * �ж�һ����Ŀ�Ƿ��Ǹ��ߵİ汾�������Ƿ��г�ͻ��
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
			project.setMessage("��ĿΪ����Ŀ����ֱ���ϴ�");
			return project;
		}
		if(modifyFlag ==2){
			CommonFullNote syncResponseNote = this.organizeSyncResponseNote(_note,null);
			project.setData(syncResponseNote);
			project.setCode("1004");
			project.setMessage("��Ŀ�ͻ��˲����ڣ�����˴��ڣ�������Ŀ��ֱ������");
			return project;
		}
		if(StringUtils.equals(_note.getDeviceId(), deviceId)){
			project.setCode("1002");
			project.setMessage("�豸IDһ�£���ֱ���ϴ�");
			return project;
		}
		//�汾��һ��
		if(syncTimestamp.compareTo(_note.getSyncTimestamp())==0){
			project.setCode("1003");
			project.setMessage("�汾��һ�£�������ϴ�");
			return project;
		}
		//����˵İ汾�Ÿ���
		if(syncTimestamp.compareTo(_note.getSyncTimestamp())<0){
			CommonFullNote noteDto = _note.convertToCommonFullNote();
			project.setData(noteDto);
			//����û�޸�
			if(modifyFlag==0){
				//�����ûɾ��
				if(_note.getDeleteFlag()==0){
					List<MindDto> minds = this.mindService.getMindListBySyncTime(DateUtils.formatDateStr(syncTimestamp));
					//------�޸��˽ڵ�Ļ������ظ߰汾�Ľڵ�
					if(null!=minds && minds.size()>0){
						project.setCode("2001");
						project.setMessage("�������޸ģ�������и��߰汾�����ҽڵ����޸ģ�������Ŀ��Ϣ�͸߸��汾�Ľڵ���Ϣ");
						noteDto.setMinds(minds);
					}else{
						project.setCode("2002");
						project.setMessage("�������޸ģ�������и��߰汾�����ǽڵ�û���޸ģ����������ֻ���һ�ڽڵ㷢���޸ģ���������Ŀ��Ϣ");
					}
				}else{
					project.setCode("2003");
					project.setMessage("�������޸ģ��������Ŀ�Ѿ�ɾ��������������Ŀ��Ϣ");
				}
			}else{
				//�����ûɾ��
				if(_note.getDeleteFlag()==0){
					//------����ȫ����Ŀ��Ϣ
					CommonFullNote syncResponseNote = this.organizeSyncResponseNote(_note,null);
					project.setData(syncResponseNote);
					project.setCode("3001");
					project.setMessage("�����Ѿ��޸ģ��������Ŀû��ɾ��������ȫ����Ŀ��Ϣ");
				}else{
					project.setCode("3002");
					project.setMessage("�����Ѿ��޸ģ����Ƿ������Ŀ�Ѿ�ɾ������������Ŀ��Ϣ");
				}
			}
		}
		return project;
	}
	
	/**
	 * ��֯���ڵ��б�Ͷ����б�
	 * */
	public CommonFullNote organizeSyncResponseNote(Note _note,CommonFullNote syncResponseNote) {
		if(null == syncResponseNote){
			syncResponseNote = _note.convertToCommonFullNote();
		}
		//����noteId��֯���ڵ��б�
		List<MindDto> minds = this.mindService.getMindListByNoteId(_note.getId());
		syncResponseNote.setMinds(minds);
		//����noteId��֯�������б�
		List<SectionDto> sections = sectionService.selectListByNoteId(_note.getId());
		syncResponseNote.setSections(sections);
		return syncResponseNote;
	}
	
	/**
	 * ����������ķ���
	 * */
	public CommonFullNote organizeNoteDtoById(String noteId) {
		Note _note = noteMapper.selectByPrimaryKey(noteId);
		return this.organizeSyncResponseNote(_note,null);
	}
	
	/**
	 * �ϴ��ӿڵ������
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
		//�������
		try {
			this.sectionService.uploadSaveSectionList(reqNote.getSections(), userId, syncTime);
		} catch (Exception e) {
			resNote.setCode("1001");
			resNote.setMessage("������䷢���쳣");
			logger.info("--------��������쳣��" + e);
			e.printStackTrace();
			return resNote;
		}
		//����ڵ�
		try {
			this.mindService.saveMindList(reqNote.getMinds(), reqNote.getId(),syncTime);
		} catch (Exception e) {
			resNote.setCode("1002");
			resNote.setMessage("����ڵ㷢���쳣");
			logger.info("--------����ڵ��쳣��" + e);
			e.printStackTrace();
			return resNote;
		}
		//������Ŀ��Ϣ
		try {
			this.saveNote(reqNote, userId, syncTime);
		} catch (Exception e) {
			resNote.setCode("1003");
			resNote.setMessage("������Ŀ����ķ����쳣");
			logger.info("--------������Ŀ������쳣��" + e);
			e.printStackTrace();
			return resNote;
		}
		return resNote;
	}
	
	/**
	 * ����������Ŀ��Ϣ
	 * */
	private int saveNote(CommonFullNote reqNote,String userId,Date syncTime){
		int res;
		Note _note = reqNote.convertToNote();
		_note.setSyncTimestamp(syncTime);
		if(null!=this.noteMapper.selectByPrimaryKey(reqNote.getId())){
			res = this.noteMapper.updateByPrimaryKey(_note);
			//�������Ŀɾ�����������Ŀ�µĶ������е���Դ
			if(null!=reqNote.getDeleteFlag() && reqNote.getDeleteFlag()==1){
				this.sectionService.clearSectionAndSource(reqNote);
			}
		}else{
			res = this.noteMapper.insert(_note);
		}
		return res;
	}

	/**
	 * ͬ���ӿ����У��
	 * */
	public Map<String,String> validateSyncRequest(SyncRequest syncRequest){
		Map<String,String> res = new HashMap<String,String>();
		res.put("code", "0000");
		if(StringUtils.isEmpty(syncRequest.getUserId())){
			res.put("code", "1001");
			res.put("message", "�û�IDΪ��");
			return res;
		}
		if(!NumberUtils.isDigits(syncRequest.getUserId())){
			res.put("code", "1002");
			res.put("message", "�û�ID�Ƿ�");
			return res;
		}
		if(StringUtils.isEmpty(syncRequest.getDeviceId())){
			res.put("code", "1003");
			res.put("message", "�豸IDΪ��");
			return res;
		}
		List<SyncRequsetNote> notes = syncRequest.getNotes();
		for(SyncRequsetNote note : notes){
			if(StringUtils.isEmpty(note.getNoteId())){
				res.put("code", "1004");
				res.put("message", "��ĿIDΪ��");
				return res;
			}
		}
		return res;
	}
	
	/**
	 * У���ϴ��ӿڵ�����
	 * */
	@Override
	public Map<String,String> validateUploadRequest(UploadRequest uploadRequest){
		Map<String,String> res = new HashMap<String,String>();
		res.put("code", "0000");
		if(StringUtils.isEmpty(uploadRequest.getUserId())){
			res.put("code", "1001");
			res.put("message", "�û�IDΪ��");
		}
		if(!NumberUtils.isDigits(uploadRequest.getUserId())){
			res.put("code", "1002");
			res.put("message", "�û�ID�Ƿ�");
			return res;
		}
		if(StringUtils.isEmpty(uploadRequest.getDeviceId())){
			res.put("code", "1003");
			res.put("message", "�豸IDΪ��");
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
	 * У���ϴ��ӿ�����Ŀ������
	 * */
	private Map<String,String> validateCommonFullNote(CommonFullNote note){
		Map<String,String> res = new HashMap<String,String>();
		res.put("code", "0000");
		if(StringUtils.isEmpty(note.getId())){
			res.put("code", "2001");
			res.put("message", "��ĿIDΪ��");
			return res;
		}
		if(StringUtils.isEmpty(note.getUserId())){
			res.put("code", "2002");
			res.put("message", "�û�IDΪ��");
			return res;
		}
		if(StringUtils.isEmpty(note.getName())){
			res.put("code", "2003");
			res.put("message", "��Ŀ����Ϊ��");
			return res;
		}
		if(StringUtils.isEmpty(note.getDeviceId())){
			res.put("code", "2004");
			res.put("message", "�豸IDΪ��");
			return res;
		}
		return res;
	}
	
	/**
	 * У���ϴ��ӿ��ж��������
	 * */
	private Map<String,String> validateSectionDto(SectionDto section){
		Map<String,String> res = new HashMap<String,String>();
		res.put("code", "0000");
		if(StringUtils.isEmpty(section.getId())){
			res.put("code", "3001");
			res.put("message", "����IDΪ��");
			return res;
		}
		if(StringUtils.isEmpty(section.getUserId())){
			res.put("code", "3002");
			res.put("message", "�û�IDΪ��");
			return res;
		}
		if(StringUtils.isEmpty(String.valueOf(section.getSectionType()))){
			res.put("code", "3003");
			res.put("message", "��������Ϊ��");
			return res;
		}
		if(section.getSectionType()==AuaisConstans.SECTION_TYPE_AUDIO){
			if(null == section.getAudioId() && section.getDeleteFlag()!=1){
				res.put("code", "3004");
				res.put("message", "����Ϊ��Ƶ���䣬������Ƶ��ϢΪ��");
				return res;
			}
		}
		if(section.getSectionType()==AuaisConstans.SECTION_TYPE_IMAGE){
			if((null == section.getImageIds() || section.getImageIds().isEmpty()) && section.getDeleteFlag()!=1){
				res.put("code", "3005");
				res.put("message", "����ΪͼƬ���䣬����ͼƬ��ϢΪ��");
				return res;
			}
		}
		if(section.getSectionType()==AuaisConstans.SECTION_TYPE_TEXT  && section.getDeleteFlag()!=1){
			if(StringUtils.isEmpty(section.getText())){
				res.put("code", "3006");
				res.put("message", "����Ϊ���ֶ��䣬����������ϢΪ��");
				return res;
			}
		}
		//���Ϊ������Ĭ��ֵ0
		if(StringUtils.isEmpty(String.valueOf(section.getUndocFlag()))){
			section.setUndocFlag((byte)0);
		}
		return res;
	}
	
	public void updateSoruce(String userId){
		
		
		
		
	}
	
	
	
	
}
