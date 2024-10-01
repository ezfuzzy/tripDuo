package com.example.tripDuo.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.dto.UserFollowDto;
import com.example.tripDuo.dto.UserProfileInfoDto;
import com.example.tripDuo.dto.UserReportDto;
import com.example.tripDuo.dto.UserReviewDto;
import com.example.tripDuo.entity.UserProfileInfo;
import com.example.tripDuo.enums.ReportTarget;

public interface UserService {

	
	// ### getUser ### 
	public List<UserDto> getUserList();
	
	public UserDto getUserById(Long userId);
	public UserDto getUserByUsername(String username);
	public UserDto getUserByPhoneNumber(String phone_num);
	public UserDto getUserByEmail(String email);

	// ### getUserProfileInfo ###
	public List<UserProfileInfo> getUserProfileInfoList();
	public Map<String, Object> getUserProfileInfoById(Long userId);
	public UserProfileInfo getUserProfileInfoByIdForPostDetailPage(Long userId);
	public UserProfileInfoDto getUserProfileInfoByUsername(String username); 
	
	// ### 중복체크 ###
	public Boolean checkExists(String checkType, String checkString);

	// ### updateUserInfo ###
	public void updateUserPrivateInfo(UserDto userDto);
	public Boolean updateUserPassword(UserDto userDto);
	public Boolean resetUserPassword(UserDto userDto);

	
	// ### updateUserProfileInfo ###
	public UserProfileInfoDto updateUserProfileInfo(UserProfileInfoDto userProfileInfoDto, MultipartFile profileImgForUpload);
	
	
	// ### deleteUser ###
	public void deleteUser(Long usreId);
	
	// ### follow ###
	public Map<String, Object> getFollowInfo(Long userId);
	public List<UserProfileInfoDto> getBlockInfo(Long userId);
	public void addFollowOrBlock(UserFollowDto userFollowDto);
	public void deleteFollowOrBlock(UserFollowDto userFollowDto);
	
	// ### review ###
	public UserReviewDto writeReview(UserReviewDto userReviewDto);
	public UserReviewDto updateReview(UserReviewDto userReviewDto);
	public void deleteReview(Long revieweeId, Long reviewerId);

	// ### report ###
	public UserReportDto report(UserReportDto userReportDto, ReportTarget targetType, Long reportedTargetId);
}
