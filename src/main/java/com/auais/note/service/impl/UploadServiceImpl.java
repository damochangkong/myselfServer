package com.auais.note.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.auais.note.dao.AudioMapper;
import com.auais.note.dao.AudioTagMapper;
import com.auais.note.dao.ImageMapper;
import com.auais.note.dao.ImageTagMapper;
import com.auais.note.pojo.Audio;
import com.auais.note.pojo.AudioTag;
import com.auais.note.pojo.Image;
import com.auais.note.pojo.ImageTag;

@Service
public class UploadServiceImpl {
	@Resource
	private ImageMapper imageMapper;
	@Resource
	private ImageTagMapper imageTagMapper;
	@Resource
	private AudioMapper audioMapper;
	@Resource
	private AudioTagMapper audioTagMapper;
	
	/**
	 * 上传图片的回调函数
	 * 
	 * */
	public void refreshImageSize(String imageUrl,String imageSize){
		Image image = new Image();
		image.setImageUrl(imageUrl);
		
		this.imageMapper.updateByPrimaryKey(image);
	}

	/**
	 * 上传图片TAG资源的回调函数
	 * 
	 * */
	public void refreshImageTagSize(String imageTagUrl,String imageSize){
		ImageTag imageTag = new ImageTag();
		imageTag.setSourceUrl(imageTagUrl);
		
		this.imageTagMapper.updateByPrimaryKey(imageTag);
	}
	
	/**
	 * 上传音频的回调函数
	 * 
	 * */
	public void refreshAudioSize(String audioUrl,String audioSize){
		Audio audio = new Audio();
		audio.setAudioUrl(audioUrl);
		
		this.audioMapper.updateByPrimaryKey(audio);
	}
	
	/**
	 * 上传音频图片的回调函数
	 * 
	 * */
	public void refreshAudioTagSize(String audioTagUrl,String imageSize){
		AudioTag audioTag = new AudioTag();
		audioTag.setImageUrl(audioTagUrl);
		
		this.audioTagMapper.updateByPrimaryKey(audioTag);
	}
	
}
