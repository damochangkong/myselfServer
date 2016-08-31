package com.auais.note.service;

import java.util.List;

import com.auais.note.dto.ImageDto;
import com.auais.note.dto.SectionDto;
import com.auais.note.pojo.Image;
import com.auais.note.pojo.SectionWithBLOBs;
import com.auais.note.pojo.Source;

public interface ImageService {

	Image getImageById(String id);
	
	List<ImageDto> organizeImageDto(String[] imageIds);
	
	void batchDealImages(List<ImageDto> imageIds,SectionDto sectionDto);
	
	void clearSource(SectionWithBLOBs tableSection);
	
	List<Source> collectSource(SectionWithBLOBs tableSection, List<Source> source);
}
