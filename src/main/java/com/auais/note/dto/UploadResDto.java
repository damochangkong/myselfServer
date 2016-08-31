package com.auais.note.dto;

public class UploadResDto {

	private String key;
	
	private String hash;
	
	private long size;
	
	private long w;
	
	private long h;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getW() {
		return w;
	}

	public void setW(long w) {
		this.w = w;
	}

	public long getH() {
		return h;
	}

	public void setH(long h) {
		this.h = h;
	}

}
