package com.auais.note.dto;

import java.util.List;

import com.auais.note.pojo.Audio;
import com.auais.note.pojo.AudioTag;
import com.auais.note.pojo.Image;
import com.auais.note.pojo.ImageTag;

public class AudioDto {
    
	private String id;

    private String audioUrl;

    private String duration;

    private List<AudioTag> audioTagIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl == null ? null : audioUrl.trim();
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration == null ? null : duration.trim();
    }

	public List<AudioTag> getAudioTagIds() {
		return audioTagIds;
	}

	public void setAudioTagIds(List<AudioTag> audioTagIds) {
		this.audioTagIds = audioTagIds;
	}
	
	public Audio convert2Audio(){
		Audio audio = new Audio();
		audio.setId(this.getId());
		audio.setDuration(this.getDuration());
		audio.setAudioUrl(this.getAudioUrl());
		audio.setAudioTagIds(this.getStringAudioTagIds());
		return audio;
	}
	
	private String getStringAudioTagIds(){
		StringBuffer _imageTagIds = new StringBuffer();
		if(null==this.getAudioTagIds() || this.getAudioTagIds().size()==0){
			return "";
		}
		for(AudioTag imageDto : this.getAudioTagIds()){
			_imageTagIds.append(imageDto.getId()).append(",");
		}
		_imageTagIds.deleteCharAt(_imageTagIds.length()-1);
		return _imageTagIds.toString();
	}

}