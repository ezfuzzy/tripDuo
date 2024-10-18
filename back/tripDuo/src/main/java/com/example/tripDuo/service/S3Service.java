package com.example.tripDuo.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
	public String uploadFile(MultipartFile multipartFile);
	public String uploadOAuthProfileImage(String imageUrl) throws IOException, URISyntaxException;
	public void deleteImage(String fileUrl);
	public String createFileName(String fileName);
	public String getFileExtension(String fileName); 
}
