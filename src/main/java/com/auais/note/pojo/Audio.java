package com.auais.note.pojo;

import com.auais.note.dto.AudioDto;

public class Audio {
    
	private String id;

    private String audioUrl;

    private String duration;

    private String audioTagIds;
    
    private Float size;
    
    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

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

    public String getAudioTagIds() {
        return audioTagIds;
    }

    public void setAudioTagIds(String audioTagIds) {
        this.audioTagIds = audioTagIds == null ? null : audioTagIds.trim();
    }
    
    public AudioDto convertToAudioDto(){
    	AudioDto audioDto = new AudioDto();
    	audioDto.setId(this.getId());
    	audioDto.setDuration(this.getDuration());
    	audioDto.setAudioUrl(this.getAudioUrl());
    	return audioDto;
    }
    
    public Source convertToSource(){
    	Source source = new Source();
    	source.setSoruceId(this.getAudioUrl());
    	source.setSize(this.getSize());
    	return source;
    }
}