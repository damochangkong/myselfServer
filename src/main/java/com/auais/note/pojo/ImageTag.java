package com.auais.note.pojo;

public class ImageTag {
    private String id;

    private Byte tagType;

    private String sourceUrl;

    private String directionX;

    private String directionY;

    private String duration;

    private Byte isLeft;

    private String text;
    
    private Float size;

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

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl == null ? null : sourceUrl.trim();
    }

    public String getDirectionX() {
        return directionX;
    }

    public void setDirectionX(String directionX) {
        this.directionX = directionX == null ? null : directionX.trim();
    }

    public String getDirectionY() {
        return directionY;
    }

    public void setDirectionY(String directionY) {
        this.directionY = directionY == null ? null : directionY.trim();
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration == null ? null : duration.trim();
    }

    public Byte getIsLeft() {
        return isLeft;
    }

    public void setIsLeft(Byte isLeft) {
        this.isLeft = isLeft;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }
    
    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }
    
    public Source convertToSource(){
    	Source source = new Source();
    	source.setSoruceId(this.getSourceUrl());
    	source.setSize(this.getSize());
    	return source;
    }
    
}