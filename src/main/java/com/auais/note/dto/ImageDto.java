package com.auais.note.dto;

import java.util.List;

import com.auais.note.pojo.Image;
import com.auais.note.pojo.ImageTag;

public class ImageDto {
    
	private String id;

    private String imageUrl;

    private List<ImageTag> imageTagIds;

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

	public List<ImageTag> getImageTagIds() {
		return imageTagIds;
	}

	public void setImageTagIds(List<ImageTag> imageTagIds) {
		this.imageTagIds = imageTagIds;
	}

	public Image convert2Image(){
		Image image = new Image();
		image.setId(this.getId());
		image.setImageTagIds(this.getStringImageTagIds());
		image.setImageUrl(this.getImageUrl());
		return image;
	}
	
	private String getStringImageTagIds(){
		if(null==this.getImageTagIds() || this.getImageTagIds().size()==0){
			return "";
		}
		StringBuffer _imageTagIds = new StringBuffer();
		for(ImageTag imageDto : this.getImageTagIds()){
			_imageTagIds.append(imageDto.getId()).append(",");
		}
		_imageTagIds.deleteCharAt(_imageTagIds.length()-1);
		return _imageTagIds.toString();
	}
	
}