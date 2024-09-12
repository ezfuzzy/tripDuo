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
import com.example.tripDuo.dto.UserFollowDto;
import com.example.tripDuo.dto.UserProfileInfoDto;
import com.example.tripDuo.dto.UserReviewDto;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.entity.UserFollow;
import com.example.tripDuo.entity.UserProfileInfo;
import com.example.tripDuo.entity.UserReview;
import com.example.tripDuo.repository.UserFollowRepository;
import com.example.tripDuo.repository.UserProfileInfoRepository;
import com.example.tripDuo.repository.UserRepository;
import com.example.tripDuo.repository.UserReviewRepository;

@Service
public class UserServiceImpl implements UserService {


	@Value("${cloud.aws.cloudfront.url}")
	private String cloudFrontUrl;
	
	private final S3Service s3Service;
	private final UserRepository userRepo;
	private final UserProfileInfoRepository userProfileInfoRepo;
	private final UserFollowRepository userFollowRepo;
	private final UserReviewRepository userReviewRepo;
	private final PasswordEncoder passwordEncoder;
	
	public UserServiceImpl(S3Service s3Service, UserRepository userRepo, 
			UserProfileInfoRepository userProfileInfoRepo, UserFollowRepository userFollowRepo, 
			PasswordEncoder passwordEncoder, UserReviewRepository userReviewRepo) {
		
		this.s3Service = s3Service;
		this.userRepo = userRepo;
		this.userProfileInfoRepo = userProfileInfoRepo;
		this.userFollowRepo = userFollowRepo;
		this.userReviewRepo = userReviewRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public List<UserDto> getUserList() {
	    return userRepo.findAll().stream()
	        .map(user -> UserDto.toDto(user))
	        .toList();
	}
	
	@Override
	public UserDto getUserById(Long id) {
		return UserDto.toDto(userRepo.findById(id).get());
	}

	@Override
	public UserDto getUserByUsername(String username) {
		return UserDto.toDto(userRepo.findByUsername(username));
	}

	@Override
	public UserDto getUserByPhoneNumber(String phoneNumber) {
		return UserDto.toDto(userRepo.findByPhoneNumber(phoneNumber));
	}

	@Override
	public UserDto getUserByEmail(String email) {
		return UserDto.toDto(userRepo.findByEmail(email));
	}
	
	@Override
	public UserProfileInfoDto getUserProfileInfoById(Long id) {
		User user = userRepo.findById(id).get();
		if(user == null) {
			return null;
		}
		
        UserProfileInfo userProfileInfo = userProfileInfoRepo.findByUserId(user.getId());
        if (userProfileInfo == null) {
            return null;  
        }

        return UserProfileInfoDto.toDto(userProfileInfo, cloudFrontUrl);
		
	}

	
	@Override	
	public UserProfileInfoDto getUserProfileInfoByUsername(String username) {
		
		User user = userRepo.findByUsername(username);
        if (user == null) {
            return null;
        }
        
        UserProfileInfo userProfileInfo = userProfileInfoRepo.findByUserId(user.getId());
        if (userProfileInfo == null) {
            return null;  
        }

        return UserProfileInfoDto.toDto(userProfileInfo, cloudFrontUrl);
	}

	
	@Override
	public Boolean checkExists(String checkType, String checkString) {
		return switch (checkType) {
			case "username" -> userRepo.existsByUsername(checkString);
			case "nickname" -> userProfileInfoRepo.existsByNickname(checkString);
			case "phoneNumber" -> userRepo.existsByPhoneNumber(checkString);
			case "email" -> userRepo.existsByEmail(checkString);
			default -> throw new IllegalArgumentException("잘못된 체크 타입입니다: " + checkType);
		};
	}

	@Override
	public void updateUserPrivateInfo(UserDto dto) {
		// TODO
	}


	@Override
	public Boolean updateUserPassword(UserDto dto) {

		if(!dto.getNewPassword().equals(dto.getConfirmPassword())) {
			return false;
		}

		UserDto existingUser = UserDto.toDto(userRepo.findByUsername(dto.getUsername()));
		// 입력된 비밀번호(dto.getPassword())와 기존 비밀번호(existingUser.getPassword()) 비교
		if (!passwordEncoder.matches(existingUser.getPassword(), dto.getPassword())) {
			return false;
			//throw new Exception("기존 비밀번호가 일치하지 않습니다.");
		}

		String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
		dto.setPassword(encodedNewPassword);
		userRepo.save(User.toEntity(dto));

		return true;
	}

	@Override
	public Boolean resetUserPassword(UserDto dto) {

		UserDto userInfo = UserDto.toDto(userRepo.findById(dto.getId()).get());

		// 새 비밀번호와 새 비밀번호 확인이 일치하는지 확인
		if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
			return false;
		}

		String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
		userInfo.setPassword(encodedNewPassword);
		userRepo.save(User.toEntity(userInfo));

		return true;
	}

	@Override
	public void updateUserProfileInfo(UserProfileInfoDto dto, MultipartFile profileImgForUpload) {

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

	    User user = userRepo.findById(dto.getUserId()).get();
	    
	    // 변경된 정보를 저장
	    userProfileInfoRepo.save(UserProfileInfo.toEntity(dto, user));

	}
	
	@Override
	public void deleteUser(Long id) {
		userRepo.deleteById(id);
	}

	@Override
	public void addFollow(UserFollowDto dto) {
		userFollowRepo.save(UserFollow.toEntity(dto));
	}

	@Override
	public void addReview(UserReviewDto dto) {
		userReviewRepo.save(UserReview.toEntity(dto));
	}

	@Override
	public void deleteReview(Long id) {
		userReviewRepo.deleteById(id);
	}

	@Override
	public void editReview(UserReviewDto dto) {
		UserReviewDto existingUserReview = UserReviewDto.toDto(userReviewRepo.findById(dto.getId()).get());
		dto.setReviewerId(existingUserReview.getReviewerId());
		dto.setRevieweeId(existingUserReview.getRevieweeId());
		dto.setCreatedAt(existingUserReview.getCreatedAt());
		
		userReviewRepo.save(UserReview.toEntity(dto));
	}
}
