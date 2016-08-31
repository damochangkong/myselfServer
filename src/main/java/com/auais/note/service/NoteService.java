package com.auais.note.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.auais.note.dto.inter.CommonFullNote;
import com.auais.note.dto.inter.SyncRequest;
import com.auais.note.dto.inter.SyncRequsetNote;
import com.auais.note.dto.inter.SyncResponseProject;
import com.auais.note.dto.inter.UploadRequest;
import com.auais.note.dto.inter.UploadResponseNote;

public interface NoteService {

	SyncResponseProject syncProject(String deviceId,SyncRequsetNote syncRequsetNote);
	
	List<SyncResponseProject> syncProjectList(String userId,String deviceId,List<SyncRequsetNote> syncRequsetNoteList);
	
	List<UploadResponseNote> uploadNotes(List<CommonFullNote> notes,String userId,Date syncTime);
	
	Map<String,String> validateSyncRequest(SyncRequest syncRequest);
	
	Map<String,String> validateUploadRequest(UploadRequest uploadRequest);
}
