package com.example.tripDuo.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {


	@Value("${cloud.aws.cloudfront.url}")
	private String cloudFrontUrl;
	
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
	    return UserRepo.findAll().stream()
	        .map(user -> UserDto.toDto(user, cloudFrontUrl))
	        .toList();
	}
	
	@Override
	public UserDto getUserById(Long id) {
		return UserDto.toDto(UserRepo.findById(id).get(), cloudFrontUrl);
	}

	@Override
	public UserDto getUserByUsername(String username) {
		return UserDto.toDto(UserRepo.findByUsername(username), cloudFrontUrl);
	}

	@Override
	public UserDto getUserByPhoneNumber(String phoneNumber) {
		return UserDto.toDto(UserRepo.findByPhoneNumber(phoneNumber), cloudFrontUrl);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		return UserDto.toDto(UserRepo.findByEmail(email), cloudFrontUrl);
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
	    Set<String> allowedExtensions = new HashSet<>(Arrays.asList(
	            "jpg", "jpeg", "png", "heic", "heif"
	    		));
	    
		
	    // 프로필 이미지가 존재하고, 파일 크기가 0이 아닌 경우 처리
	    if (profileImgForUpload != null && !profileImgForUpload.isEmpty()) {

	        String originalFilename = profileImgForUpload.getOriginalFilename();
	        if (originalFilename == null) {
	            throw new IllegalArgumentException("파일 이름이 누락되었습니다.");
	        }
	    	
	        // 파일 확장자 추출 및 검증
	        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
	        if (!allowedExtensions.contains(fileExtension)) {
	            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다.");
	        }

	        // 파일 크기 제한 5MB
	        long maxFileSize = 5 * 1024 * 1024;
	        if (profileImgForUpload.getSize() > maxFileSize) {
	            throw new IllegalArgumentException("파일 크기가 너무 큽니다. 최대 5MB까지 허용됩니다.");
	        }
	        
	        // MIME 타입 검사
	        String mimeType = profileImgForUpload.getContentType();
	        if (mimeType == null || !(mimeType.equals("image/jpeg") || mimeType.equals("image/png") || mimeType.equals("image/heic") || mimeType.equals("image/heif"))) {
	            throw new IllegalArgumentException("허용되지 않는 MIME 타입입니다.");
	        }
	    	
	        // s3에 저장되는 profilePicture ( 파일 이름.확장자 ) dto에 저장
	        dto.setProfilePicture(s3Service.uploadFile(profileImgForUpload));
	    }
	    // 기존 유저 정보 가져오기
		UserDto existingUser = UserDto.toDto(UserRepo.findById(dto.getId()).get(), "");

	    // 비밀번호는 기존 것을 유지
	    dto.setPassword(existingUser.getPassword());
	    
	    // 변경된 정보를 저장
	    UserRepo.save(User.toEntity(dto));

	}

	@Override
	public Boolean updateUserPassword(UserDto dto) {

		if(!dto.getNewPassword().equals(dto.getConfirmPassword())) {
			return false;
		}

		UserDto existingUser = UserDto.toDto(UserRepo.findByUsername(dto.getUsername()), "");
		// 입력된 비밀번호(dto.getPassword())와 기존 비밀번호(existingUser.getPassword()) 비교
		if (!passwordEncoder.matches(existingUser.getPassword(), dto.getPassword())) {
			return false;
			//throw new Exception("기존 비밀번호가 일치하지 않습니다.");
		}

		String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
		dto.setPassword(encodedNewPassword);
		UserRepo.save(User.toEntity(dto));

		return true;
	}

	@Override
	public Boolean resetUserPassword(UserDto dto) {

		UserDto userInfo = UserDto.toDto(UserRepo.findById(dto.getId()).get(), "");

		// 새 비밀번호와 새 비밀번호 확인이 일치하는지 확인
		if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
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
