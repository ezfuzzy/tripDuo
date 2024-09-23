package com.example.tripDuo.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.dto.UserFollowDto;
import com.example.tripDuo.dto.UserProfileInfoDto;
import com.example.tripDuo.dto.UserReviewDto;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.entity.UserFollow;
import com.example.tripDuo.entity.UserProfileInfo;
import com.example.tripDuo.entity.UserReview;
import com.example.tripDuo.enums.FollowType;
import com.example.tripDuo.repository.UserFollowRepository;
import com.example.tripDuo.repository.UserProfileInfoRepository;
import com.example.tripDuo.repository.UserRepository;
import com.example.tripDuo.repository.UserReviewRepository;
import com.example.tripDuo.util.JwtUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {


	@Value("${cloud.aws.cloudfront.profile_picture_url}")
	private String PROFILE_PICTURE_CLOUDFRONT_URL;
	
	private final JwtUtil jwtUtil;
	private final S3Service s3Service;
	private final UserRepository userRepo;
	private final UserProfileInfoRepository userProfileInfoRepo;
	private final UserFollowRepository userFollowRepo;
	private final UserReviewRepository userReviewRepo;
	private final PasswordEncoder passwordEncoder;
	
	public UserServiceImpl(JwtUtil jwtUtil, S3Service s3Service, UserRepository userRepo, 
			UserProfileInfoRepository userProfileInfoRepo, UserFollowRepository userFollowRepo, 
			PasswordEncoder passwordEncoder, UserReviewRepository userReviewRepo) {
		
		this.jwtUtil = jwtUtil;
		this.s3Service = s3Service;
		this.userRepo = userRepo;
		this.userProfileInfoRepo = userProfileInfoRepo;
		this.userFollowRepo = userFollowRepo;
		this.userReviewRepo = userReviewRepo;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * @date : 2024. 9. 14.
	 * @user : 김민준
	 * getUserList : admin dashboard 페이지에서 호출할 api
	 *
	 * @return
	 * TODO : role이나 조건, 검색등의 옵션도 제공 예정
	 */
	@Override
	public List<UserDto> getUserList() {
	    return userRepo.findAll().stream()
	        .map(user -> UserDto.toDto(user))
	        .toList();
	}

	@Override
	public UserDto getUserById(Long userId) {
		return UserDto.toDto(userRepo.findById(userId).get());
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
	public UserProfileInfoDto getUserProfileInfoById(Long userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found"));
		
		Long follwerCount = userFollowRepo.countByFollowerUserIdAndFollowType(userId, FollowType.FOLLOW);
		Long follweeCount = userFollowRepo.countByFolloweeUserIdAndFollowType(userId, FollowType.FOLLOW);
		
        UserProfileInfo userProfileInfo = userProfileInfoRepo.findByUserId(user.getId());
        
        UserProfileInfoDto upiDto = UserProfileInfoDto.toDto(userProfileInfo, PROFILE_PICTURE_CLOUDFRONT_URL);
        upiDto.setFollowerCount(follwerCount);
        upiDto.setFolloweeCount(follweeCount);
        
        return upiDto;
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

        return UserProfileInfoDto.toDto(userProfileInfo, PROFILE_PICTURE_CLOUDFRONT_URL);
	}

	/**
	 * @date : 2024. 9. 14.
	 * @user : 김민준
	 * checkExists : 중복체크 메소드
	 *
	 * @param checkType
	 * @param checkString
	 * @return
	 */
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

	/**
	 * @date : 2024. 9. 14.
	 * @user : 김민준
	 * updateUserPrivateInfo : user 엔티티의 정보 변경 메소드
	 *
	 * @param userDto
	 */
	@Override
	public void updateUserPrivateInfo(UserDto userDto) {
		// TODO
	}

	/**
	 * @date : 2024. 9. 14.
	 * @user : 김민준
	 * updateUserPassword : password 변경 메소드
	 *
	 * @param userDto
	 * @return
	 */
	@Override
	public Boolean updateUserPassword(UserDto userDto) {

		if(!userDto.getNewPassword().equals(userDto.getConfirmPassword())) {
			return false;
		}

		UserDto existingUser = UserDto.toDto(userRepo.findByUsername(userDto.getUsername()));
		// 입력된 비밀번호(userDto.getPassword())와 기존 비밀번호(existingUser.getPassword()) 비교
		if (!passwordEncoder.matches(existingUser.getPassword(), userDto.getPassword())) {
			return false;
			//throw new Exception("기존 비밀번호가 일치하지 않습니다.");
		}

		String encodedNewPassword = passwordEncoder.encode(userDto.getNewPassword());
		userDto.setPassword(encodedNewPassword);
		userRepo.save(User.toEntity(userDto));

		return true;
	}

	/**
	 * @date : 2024. 9. 14.
	 * @user : 김민준
	 * resetUserPassword : 비밀번호 초기화 메소드
	 *
	 * @param userDto
	 * @return
	 */
	@Override
	public Boolean resetUserPassword(UserDto userDto) {

		UserDto userInfo = UserDto.toDto(userRepo.findById(userDto.getId()).get());

		// 새 비밀번호와 새 비밀번호 확인이 일치하는지 확인
		if (!userDto.getNewPassword().equals(userDto.getConfirmPassword())) {
			return false;
		}

		String encodedNewPassword = passwordEncoder.encode(userDto.getNewPassword());
		userInfo.setPassword(encodedNewPassword);
		userRepo.save(User.toEntity(userInfo));

		return true;
	}

	/**
	 * @date : 2024. 9. 14.
	 * @user : 김민준
	 * updateUserProfileInfo : UserProfileInfo 엔티티의 정보 변경 메소드
	 *
	 * @param userProfileInfoDto
	 * @param profileImgForUpload
	 */
	@Override
	public UserProfileInfoDto updateUserProfileInfo(UserProfileInfoDto userProfileInfoDto, MultipartFile profileImgForUpload) {

		// 허용된 파일 확장자 목록
	    Set<String> allowedExtensions = new HashSet<>(Arrays.asList(
	            "jpg", "jpeg", "png", "heic", "heif"
	    		));
	    
	    if(userProfileInfoDto.getProfilePicture() != null) {
	        String pictureName = userProfileInfoDto.getProfilePicture().replace(PROFILE_PICTURE_CLOUDFRONT_URL, "");
	        userProfileInfoDto.setProfilePicture(pictureName);
	    }
	    
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
	    	
	        // s3에 저장되는 profilePicture ( 파일 이름.확장자 ) userProfileInfoDto에 저장
	        userProfileInfoDto.setProfilePicture(s3Service.uploadFile(profileImgForUpload));
	    }

	    User user = userRepo.findById(userProfileInfoDto.getUserId()).get();
	    
	    // 변경된 정보를 저장
	    userProfileInfoRepo.save(UserProfileInfo.toEntity(userProfileInfoDto, user));
	    
	    // 업데이트된 정보를 처리하기 위해 front로 정보 return
	    userProfileInfoDto.setProfilePicture(PROFILE_PICTURE_CLOUDFRONT_URL + userProfileInfoDto.getProfilePicture());
	   
	    String token = jwtUtil.generateToken(user.getUsername());
		
	    userProfileInfoDto.setToken(token);
	    return userProfileInfoDto;
	}
	
	@Override
	public void deleteUser(Long userId) {
		userRepo.deleteById(userId);
	}
	

	// ### follow part ### 
	
	/**
	 * @date : 2024. 9. 20.
	 * @user : 유병한
	 * getFollowerProfileInfoList: followeeUser를 팔로우한 유저들의 프로필 정보 받아오기
	 * 
	 * @param followeeUserId
	 * @return List<UserProfileInfoDto>
	 */
	@Override
	public List<UserProfileInfoDto> getFollowerProfileInfoList(Long followeeUserId) {
	    // 1. Followee를 팔로우하는 특정 FollowType의 Follower들의 ID 조회
	    List<Long> userIds = userFollowRepo.findFollowerUserIdsByFolloweeUserIdAndFollowType(followeeUserId, FollowType.FOLLOW);
	    
	    // 2. 조회된 Follower들의 프로필 정보 가져오기
	    return userProfileInfoRepo.findProfilesByUserIds(userIds);
	}

	/**
	 * @date : 2024. 9. 20.
	 * @user : 유병한
	 * getFolloweeProfileInfoList: followerUser가 팔로우/차단한 유저들의 프로필 정보 받아오기
	 * 
	 * @param followerUserId
	 * @param followType
	 * @return List<UserProfileInfoDto>
	 */
	@Override
	public List<UserProfileInfoDto> getFolloweeProfileInfoList(Long followerUserId, FollowType followType) {
	    // 1. Follower가 팔로우하는 특정 FollowType의 Followee들의 ID 조회
	    List<Long> userIds = userFollowRepo.findFolloweeUserIdsByFollowerUserIdAndFollowType(followerUserId, followType);
	    
	    // 2. 조회된 Followee들의 프로필 정보 가져오기
	    return userProfileInfoRepo.findProfilesByUserIds(userIds);
	}

	/**
	 * @date : 2024. 9. 15.
	 * @user : 유병한
	 * addFollowOrBlock: 팔로우/차단 하기
	 * 
	 * @param userFollowDto
	 */
	@Override
	public void addFollowOrBlock(UserFollowDto userFollowDto) {
	    UserFollow userFollow = UserFollow.toEntity(userFollowDto);
	    userFollowRepo.save(userFollow);
	}

	/**
	 * @date : 2024. 9. 15.
	 * @user : 유병한
	 * deleteFollowOrBlock: 팔로우/차단 해제하기
	 * 
	 * @param userFollowDto
	 */
	@Transactional
	@Override
	public void deleteFollowOrBlock(UserFollowDto userFollowDto) {
	    userFollowRepo.deleteByFolloweeUserIdAndFollowTypeAndFollowerUserId(
	            userFollowDto.getFolloweeUserId(),
	            userFollowDto.getFollowType(),
	            userFollowDto.getFollowerUserId());
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
	public void updateReview(UserReviewDto dto) {
		UserReviewDto existingUserReview = UserReviewDto.toDto(userReviewRepo.findById(dto.getId()).get());
		dto.setReviewerId(existingUserReview.getReviewerId());
		dto.setRevieweeId(existingUserReview.getRevieweeId());
		dto.setCreatedAt(existingUserReview.getCreatedAt());
		
		userReviewRepo.save(UserReview.toEntity(dto));
	}
}