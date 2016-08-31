package com.auais.note.pojo;

public class AudioTag {
    
	private String id;

    private Byte tagType;

    private String imageUrl;

    private String timePoint;

    private String text;
    
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

    public Byte getTagType() {
        return tagType;
    }

    public void setTagType(Byte tagType) {
        this.tagType = tagType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint == null ? null : timePoint.trim();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }
    
    public Source convertToSource(){
    	Source source = new Source();
    	source.setSoruceId(this.getImageUrl());
    	source.setSize(this.getSize());
    	return source;
    }
}