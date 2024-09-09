package com.example.tripDuo.service;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Value("${file.location}")
	private String fileLocation;

	private final S3Service s3Service;
	private final UserRepository UserRepo;
	private final PasswordEncoder passwordEncoder;

	
	
	public UserServiceImpl(S3Service s3Service, UserRepository UserRepo, PasswordEncoder passwordEncoder) {
		this.s3Service = s3Service;
		this.UserRepo = UserRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public List<UserDto> getUserList() {
		return UserRepo.findAll().stream().map(UserDto::toDto).toList();
	}
	
	@Override
	public UserDto getUserById(Long id) {
		return UserDto.toDto(UserRepo.findById(id).get());
	}

	@Override
	public UserDto getUserByUsername(String username) {
		return UserDto.toDto(UserRepo.findByUsername(username));
	}

	@Override
	public UserDto getUserByPhoneNumber(String phoneNumber) {
		return UserDto.toDto(UserRepo.findByPhoneNumber(phoneNumber));
	}

	@Override
	public UserDto getUserByEmail(String email) {
		return UserDto.toDto(UserRepo.findByEmail(email));
	}

	@Override
	public Boolean checkExists(String checkType, String checkString) {
		return switch (checkType) {
			case "username" -> UserRepo.existsByUsername(checkString);
			case "nickname" -> UserRepo.existsByNickname(checkString);
			case "phoneNumber" -> UserRepo.existsByPhoneNumber(checkString);
			case "email" -> UserRepo.existsByEmail(checkString);
			default -> throw new IllegalArgumentException("잘못된 체크 타입입니다: " + checkType);
		};
	}

	@Override
	public void updateUserInfo(UserDto dto, MultipartFile profileImgForUpload) {

		// 허용된 파일 확장자 목록
		Set<String> allowedExtensions = new HashSet<>();
		allowedExtensions.add("jpg");
		allowedExtensions.add("jpeg");
		allowedExtensions.add("png");

		
		/*
		 * // 프로필 이미지가 존재하고, 파일 크기가 0이 아닌 경우 처리 if (profileImgForUpload != null &&
		 * !profileImgForUpload.isEmpty()) {
		 * 
		 * String originalFileName = profileImgForUpload.getOriginalFilename();
		 * 
		 * // 파일 확장자 검증 String fileExtension =
		 * originalFileName.substring(originalFileName.lastIndexOf(".") +
		 * 1).toLowerCase(); if (!allowedExtensions.contains(fileExtension)) { throw new
		 * IllegalArgumentException("허용되지 않은 파일 확장자입니다: " + fileExtension); }
		 * 
		 * String saveFileName = UUID.randomUUID().toString(); String filePath =
		 * fileLocation + File.separator + saveFileName; System.out.println(filePath);
		 * 
		 * // 파일을 지정된 경로로 저장 try { File file = new File(filePath);
		 * profileImgForUpload.transferTo(file); dto.setProfilePicture(saveFileName); }
		 * catch (IOException e) { e.printStackTrace(); // throw new
		 * CustomFileUploadException("File upload failed", e); } }
		 */
		
		
		
	    // 프로필 이미지가 존재하고, 파일 크기가 0이 아닌 경우 처리
		String profilePictureName = s3Service.uploadFile(profileImgForUpload);
		
		
	    // 기존 유저 정보 가져오기
		UserDto existingUser = UserDto.toDto(UserRepo.findById(dto.getId()).get());

	    // 비밀번호는 기존 것을 유지
	    dto.setPassword(existingUser.getPassword());
	    dto.setProfilePicture(profilePictureName);
	    
	    // 변경된 정보를 저장
	    UserRepo.save(User.toEntity(dto));

	}

	@Override
	public Boolean updateUserPassword(UserDto dto) {

		if(!dto.getNewPassword().equals(dto.getNewConfirmPassword())) {
			return false;
		}

		UserDto existingUser = UserDto.toDto(UserRepo.findByUsername(dto.getUsername()));
		// 입력된 비밀번호(dto.getPassword())와 기존 비밀번호(existingUser.getPassword()) 비교
		if (!passwordEncoder.matches(existingUser.getPassword(), dto.getPassword())) {
			return false;
			//throw new Exception("기존 비밀번호가 일치하지 않습니다.");
		}

		// 새 비밀번호와 새 비밀번호 확인이 일치하는지 확인
		if (!dto.getNewPassword().equals(dto.getNewConfirmPassword())) {
			return false;
		}

		String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
		dto.setPassword(encodedNewPassword);
		UserRepo.save(User.toEntity(dto));

		return true;
	}

	@Override
	public Boolean resetUserPassword(UserDto dto) {

		UserDto userInfo = UserDto.toDto(UserRepo.findById(dto.getId()).get());

		// 새 비밀번호와 새 비밀번호 확인이 일치하는지 확인
		if (!dto.getNewPassword().equals(dto.getNewConfirmPassword())) {
			return false;
		}

		String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
		userInfo.setPassword(encodedNewPassword);
		UserRepo.save(User.toEntity(userInfo));

		return true;
	}


	@Override
	public void deleteUser(Long id) {
		UserRepo.deleteById(id);
	}


}
