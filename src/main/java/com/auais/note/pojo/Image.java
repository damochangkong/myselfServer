package com.auais.note.pojo;

import com.auais.note.dto.ImageDto;

public class Image {
    private String id;

    private String imageUrl;
    
    private Float size;

    private String imageTagIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }
    
    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public String getImageTagIds() {
        return imageTagIds;
    }

    public void setImageTagIds(String imageTagIds) {
        this.imageTagIds = imageTagIds == null ? null : imageTagIds.trim();
    }
    
    public ImageDto convertToImageDto(){
    	ImageDto res = new ImageDto();
    	res.setId(this.getId());
    	res.setImageUrl(this.getImageUrl());
    	return res;
    }
    
    public Source convertToSource(){
    	Source source = new Source();
    	source.setSoruceId(this.getImageUrl());
    	source.setSize(this.getSize());
    	return source;
    }
}