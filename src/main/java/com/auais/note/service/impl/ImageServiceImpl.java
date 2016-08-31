package com.auais.note.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.auais.note.dao.ImageMapper;
import com.auais.note.dao.ImageTagMapper;
import com.auais.note.dao.SectionMapper;
import com.auais.note.dto.ImageDto;
import com.auais.note.dto.SectionDto;
import com.auais.note.pojo.Image;
import com.auais.note.pojo.ImageTag;
import com.auais.note.pojo.SectionWithBLOBs;
import com.auais.note.pojo.Source;
import com.auais.note.service.ImageService;
import com.auais.note.util.AuaisConstans;

@Service("imageService")
public class ImageServiceImpl implements ImageService {
	@Resource
	private ImageMapper imageMapper;
	@Resource
	private ImageTagMapper imageTagMapper;
	@Resource
	private SectionMapper sectionMapper;
	
	
	@Override
	public Image getImageById(String id) {
		
		return this.imageMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<ImageDto> organizeImageDto(String[] imageIds){
		if(imageIds.length==0){
			return null;
		}
		List<ImageDto> result = new ArrayList<ImageDto>();
		List<Image> list = this.imageMapper.selectListByIds(imageIds);
		if(null==list || list.size()==0){
			return result;
		}
		ImageDto _temp = null;
		for(Image _curr : list){
			_temp = _curr.convertToImageDto();
			List<ImageTag> tagList = null;
			String tagIds = _curr.getImageTagIds();
			System.out.println(tagIds);
			if(StringUtils.isNotEmpty(tagIds)){
				tagList = imageTagMapper.selectListByIds(StringUtils.split(tagIds,","));
			}
			_temp.setImageTagIds(tagList);
			result.add(_temp);
		}
		return result;
	}

	/**
	 * ����ͼƬ��Դ���������Դ��¼���ٲ����µ���Դ
	 * 
	 * */
	@Override
	public void batchDealImages(List<ImageDto> imageIds,SectionDto sectionDto){
		this.deleteSource(null,sectionDto.getId());
		//ɾ����־��ֱ�ӷ���
		if(sectionDto.getDeleteFlag()==1){
			return;
		}
		//����û�д���Դ��ֱ�ӷ���
		if(null==imageIds || imageIds.size()==0){
			return;
		}
		List<ImageTag> imageTagIds = new ArrayList<ImageTag>();
		List<Image> images = new ArrayList<Image>();
		for(ImageDto _curr : imageIds){
			images.add(_curr.convert2Image());
			if(null!=_curr.getImageTagIds() && _curr.getImageTagIds().size()>0){
				imageTagIds.addAll(_curr.getImageTagIds());
			}
		}
		//�ȱ���tag��Ϣ
		this.imageTagMapper.deleteByIds(imageTagIds);
		this.imageTagMapper.batchInsert(imageTagIds);
		//����image��Ϣ
		this.imageMapper.deleteByIds(images);
		this.imageMapper.batchInsert(images);
	}
	
	/**
	 * ��ɾ��������Դ
	 * 
	 * */
	private void deleteSource(SectionWithBLOBs tableSection, String sectionId){
		//ɾ������Ļ�����ɾ��ͼƬ��Դ
		if(null == tableSection){
			tableSection = sectionMapper.selectByPrimaryKey(sectionId);
		}
		if(null == tableSection){
			return;
		}
		if(StringUtils.isNotEmpty(tableSection.getImageIds())){
			String[] images = StringUtils.split(tableSection.getImageIds(),",");
			List<Image> imageList = this.imageMapper.selectListByIds(images);
			String imageTags = "";
			for(Image image : imageList){
				imageTags = image.getImageTagIds();
				if(StringUtils.isNotEmpty(imageTags)){
					this.imageTagMapper.deleteByIdsArray(StringUtils.split(image.getImageTagIds(),","));
				}
			}
			this.imageMapper.deleteByIdsArray(images);
		}
	}

	@Override
	public void clearSource(SectionWithBLOBs tableSection){
		this.deleteSource(tableSection, null);
	}
	
	/**
	 * ��֯��Դ
	 * 
	 * */
	@Override
	public List<Source> collectSource(SectionWithBLOBs tableSection, List<Source> source){
		String userId = tableSection.getUserId();
		if(StringUtils.isNotEmpty(tableSection.getImageIds())){
			String[] images = StringUtils.split(tableSection.getImageIds(),",");
			List<Image> imageList = this.imageMapper.selectListByIds(images);
			String imageTags = "";
			Source _source = null;
			for(Image image : imageList){
				imageTags = image.getImageTagIds();
				if(StringUtils.isNotEmpty(imageTags)){
					 List<ImageTag> tags = 
							 this.imageTagMapper.selectListByIds(StringUtils.split(image.getImageTagIds(),","));
					 for(ImageTag _curr : tags){
						 if(_curr.getTagType() != AuaisConstans.IMAGE_TAG_TYPE_TEXT){
							 _source = _curr.convertToSource();
							 _source.setUserId(userId);
							 source.add(_source);
						 }
					 }
				}
				_source = image.convertToSource();
				_source.setUserId(userId);
				source.add(_source);
			}
		}
		return source;
	}
	
}
