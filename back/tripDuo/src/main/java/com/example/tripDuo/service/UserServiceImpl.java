package com.example.tripDuo.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
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
	public List<UserProfileInfo> getUserProfileInfoList() {
		return userProfileInfoRepo.findAll(); 
	}

	
	@Override
	public Map<String, Object> getUserProfileInfoById(Long userId) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Long currentUserId = userRepo.findByUsername(username).getId();
		
		// 상대방이 나를 follow / block했는지
		UserFollow myUserFollow = userFollowRepo
				.findByFolloweeUserProfileInfo_User_IdAndFollowerUserProfileInfo_User_Id(
						userId, currentUserId);
		
		// 내가 상대방을 follow / block 했는지 
		UserFollow theirUserfollow = userFollowRepo
				.findByFolloweeUserProfileInfo_User_IdAndFollowerUserProfileInfo_User_Id(
						currentUserId, userId);
				
		String myFollowType, theirFollowType;
		if(myUserFollow != null) {
			myFollowType = myUserFollow.getFollowType().toString();
		} else {
			myFollowType = "";
		}
		
		if(theirUserfollow != null) {
			theirFollowType = theirUserfollow.getFollowType().toString();
		} else {
			theirFollowType = "";
		}
		
		
		if(theirFollowType.equals("BLOCK")) { // 만약 차단당했으면 정보가 필요없으므로 불러오지 않는다
			return Map.of(
						"myFollowType", myFollowType,
						"theirFollowType", theirFollowType
					);
		} else { // 차단 당하지 않았으면 정보를 불러온다
			User user = userRepo.findById(userId)
					.orElseThrow(() -> new EntityNotFoundException("User not found"));
			
			Long follweeCount = userFollowRepo.countByFollowerUserProfileInfo_User_IdAndFollowType(userId, FollowType.FOLLOW);
			Long follwerCount = userFollowRepo.countByFolloweeUserProfileInfo_User_IdAndFollowType(userId, FollowType.FOLLOW);
			
	        UserProfileInfo userProfileInfo = userProfileInfoRepo.findByUserId(user.getId());
	        
	        UserProfileInfoDto upiDto = UserProfileInfoDto.toDto(userProfileInfo, PROFILE_PICTURE_CLOUDFRONT_URL);
	        upiDto.setFolloweeCount(follweeCount);
	        upiDto.setFollowerCount(follwerCount);
	        
			// 리뷰 목록을 createdAt 최신순으로 조회
			List<UserReview> userReviewList = userReviewRepo.findByRevieweeId(userId, Sort.by(Sort.Direction.DESC, "createdAt"));
			
			// UserReview를 UserReviewDto로 변환
		    List<UserReviewDto> userReviewDtoList = userReviewList.stream()
		        .map(item -> UserReviewDto.toDto(item, PROFILE_PICTURE_CLOUDFRONT_URL))
		        .collect(Collectors.toList());
		    
		    Map<String, Object> map = Map.of(
			    		"userProfileInfo", upiDto,
			    		"userReviewList", userReviewDtoList,
			    		"myFollowType", myFollowType,
						"theirFollowType", theirFollowType
		    		);
		    
	    	return map;
		}
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
		userRepo.save(User.toEntity(userDto));
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
		
	    userProfileInfoDto.setToken("Bearer+" + token);
	    return userProfileInfoDto;
	}
	
	@Override
	public void deleteUser(Long userId) {
		userRepo.deleteById(userId);
	}
	

	// ### follow ### 
	
	/**
	 * @date : 2024. 9. 25.
	 * @user : 유병한
	 * getFollowInfo: 유저의 팔로워/팔로이 리스트 받아오기
	 * 
	 * @param userId
	 * @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> getFollowInfo(Long userId) {
		List<UserFollow> tmpFolloweeList = userFollowRepo.findByFollowerUserProfileInfo_User_IdAndFollowType(userId, FollowType.FOLLOW);
		List<UserFollow> tmpFollowerList = userFollowRepo.findByFolloweeUserProfileInfo_User_IdAndFollowType(userId, FollowType.FOLLOW);
		
		List<UserProfileInfoDto> followeeList = tmpFolloweeList.stream().map(item -> UserProfileInfoDto.toDto(item.getFolloweeUserProfileInfo(), PROFILE_PICTURE_CLOUDFRONT_URL)).toList();
		List<UserProfileInfoDto> followerList = tmpFollowerList.stream().map(item -> UserProfileInfoDto.toDto(item.getFollowerUserProfileInfo(), PROFILE_PICTURE_CLOUDFRONT_URL)).toList();
		
	    Map<String, Object> map = Map.of(
	    	"followeeList", followeeList,
			"followerList", followerList
		);
	    		
	    return map;
	}

	/**
	 * @date : 2024. 9. 25.
	 * @user : 유병한
	 * getBlockInfo: 유저가 차단한 차단 리스트 받아오기
	 * 
	 * @param userId
	 * @return List<UserProfileInfoDto>
	 */
	@Override
	public List<UserProfileInfoDto> getBlockInfo(Long userId) {
	    List<UserFollow> tmpBlockedUserList = userFollowRepo.findByFollowerUserProfileInfo_User_IdAndFollowType(userId, FollowType.BLOCK);
	    
	    List<UserProfileInfoDto> blockedUserList = tmpBlockedUserList.stream().map(item -> UserProfileInfoDto.toDto(item.getFolloweeUserProfileInfo(), PROFILE_PICTURE_CLOUDFRONT_URL)).toList();
	    return blockedUserList;
	}

	/**
	 * @date : 2024. 9. 25.
	 * @user : 유병한
	 * addFollowOrBlock: 팔로우/차단 하기
	 * 
	 * @param userFollowDto
	 */
	@Override
	@Transactional
	public void addFollowOrBlock(UserFollowDto userFollowDto) {
		
		UserFollow tempUserFollow = userFollowRepo.findByFolloweeUserProfileInfo_User_IdAndFollowerUserProfileInfo_User_Id(userFollowDto.getFolloweeUserId(), userFollowDto.getFollowerUserId());
		
		if(tempUserFollow != null) { // 기존 관계가 있으면 update - createdAt 갱신
			tempUserFollow.updateFollowType(userFollowDto.getFollowType());
		} else { // 기존 관계가 없으면 새로 데이터 저장 
			UserProfileInfo followeeProfileInfo = userProfileInfoRepo.findByUserId(userFollowDto.getFolloweeUserId());
			UserProfileInfo followerProfileInfo = userProfileInfoRepo.findByUserId(userFollowDto.getFollowerUserId());	
			System.out.println("6번이어야함 : " + userFollowDto.getFollowerUserId());
			System.out.println("6번이 아니어야함 : " + userFollowDto.getFolloweeUserId());
			
			UserFollow userFollow = UserFollow.toEntity(userFollowDto, followeeProfileInfo, followerProfileInfo);
			userFollowRepo.save(userFollow);
		}
	}

	/**
	 * @date : 2024. 9. 25.
	 * @user : 유병한
	 * deleteFollowOrBlock: 팔로우/차단 해제하기
	 * 
	 * @param userFollowDto
	 */
	@Transactional
	@Override
	public void deleteFollowOrBlock(UserFollowDto userFollowDto) {
	    userFollowRepo.deleteByFolloweeUserProfileInfo_User_IdAndFollowTypeAndFollowerUserProfileInfo_User_Id(
	            userFollowDto.getFolloweeUserId(),
	            userFollowDto.getFollowType(),
	            userFollowDto.getFollowerUserId());
	}
	
	// ### review ### 

	/**
	 * @date : 2024. 9. 23.
	 * @user : 유병한
	 * writeReview: 리뷰 작성하기
	 * 
	 * @param userReviewDto
	 */
	@Override
	public void writeReview(UserReviewDto userReviewDto) {
		UserProfileInfo reviewerProfileInfo = userProfileInfoRepo.findByUserId(userReviewDto.getReviewerId());
		
		UserReview userReview = UserReview.toEntity(userReviewDto, reviewerProfileInfo);
		userReviewRepo.save(userReview);
	}
	
	/**
	 * @date : 2024. 9. 23.
	 * @user : 유병한
	 * updateReview: 리뷰 수정하기
	 * 
	 * @param userReviewDto
	 */
	@Override
	@Transactional
	public void updateReview(UserReviewDto userReviewDto) {
	    UserReview existUserReview = userReviewRepo.findByRevieweeIdAndReviewerUserProfileInfo_User_Id(userReviewDto.getRevieweeId(), userReviewDto.getReviewerId());
	    if(existUserReview == null) {
	    	throw new EntityNotFoundException("리뷰가 존재하지 않습니다");
	    }
	    
	    existUserReview.updateContent(userReviewDto.getContent());
	    existUserReview.updateTags(userReviewDto.getTags());
	    existUserReview.updateRating(userReviewDto.getRating());
	}
	
	/**
	 * @date : 2024. 9. 23.
	 * @user : 유병한
	 * deleteReview: reviewerId가 revieweeId에게 작성한 리뷰 삭제하기
	 * 
	 * @param revieweeId
	 * @param reviewerId
	 */
	@Transactional
	@Override
	public void deleteReview(Long revieweeId, Long reviewerId) {
		userReviewRepo.deleteByRevieweeIdAndReviewerUserProfileInfo_User_Id(revieweeId, reviewerId);
	}
}