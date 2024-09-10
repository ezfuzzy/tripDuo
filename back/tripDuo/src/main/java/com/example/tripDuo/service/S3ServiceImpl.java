package com.example.tripDuo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3ServiceImpl implements S3Service{
	 
	private final AmazonS3 amazonS3; 
	 
	 @Value("${cloud.aws.s3.bucket}")
	 private String bucket;
	    
	  // AmazonS3 빈을 생성자로 주입
	  public S3ServiceImpl(AmazonS3 amazonS3) {
	      this.amazonS3 = amazonS3;
	  }
	  
	@Override
	public String uploadFile(MultipartFile multipartFile) {
		  String fileName = createFileName(multipartFile.getOriginalFilename());
		  
	      ObjectMetadata metadata = new ObjectMetadata();
	      metadata.setContentLength(multipartFile.getSize());
	      metadata.setContentType(multipartFile.getContentType());

	      try (InputStream inputStream = multipartFile.getInputStream()) {
	    	  amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
	      } catch (IOException e) {
	          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
	      }
	      //return amazonS3.getUrl(bucket, fileName).toString();
	      return fileName;
	}

	@Override
	public void deleteImage(String fileUrl) {
		// 이미지 수정으로 인해 기존 이미지 삭제 메소드
	        String splitStr = ".com/";
	        String fileName = fileUrl.substring(fileUrl.lastIndexOf(splitStr) + splitStr.length());

	        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
		
	}

	@Override
	public String createFileName(String fileName) {
		return UUID.randomUUID().toString().concat(getFileExtension(fileName));
	}

	@Override
	public String getFileExtension(String fileName) {
		try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException se) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
	}

}
