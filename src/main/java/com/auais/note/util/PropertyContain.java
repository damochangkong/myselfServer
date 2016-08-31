package com.auais.note.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyContain {

	private String accessKey;
	
	private String secretKey;
	
	private String downloadUrl;

	public String getAccessKey() {
		return accessKey;
	}

	@Value("#{propertiesReader[ACCESS_KEY]}")
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	@Value("#{propertiesReader[SECRET_KEY]}")
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	@Value("#{propertiesReader[downloadUrl]}")
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	
	
}
