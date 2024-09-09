package com.example.tripDuo.service;

import java.io.File;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
	public String uploadFile(MultipartFile multipartFile);
	public void deleteImage(String fileUrl);
	public String createFileName(String fileName);
	public String getFileExtension(String fileName);
}
