package com.example.tripDuo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.dto.UserFollowDto;
import com.example.tripDuo.dto.UserProfileInfoDto;
import com.example.tripDuo.dto.UserReviewDto;
import com.example.tripDuo.enums.FollowType;

public interface UserService {

	
	// ### getUser ### 
	public List<UserDto> getUserList();
	
	public UserDto getUserById(Long userId);
	public UserDto getUserByUsername(String username);
	public UserDto getUserByPhoneNumber(String phone_num);
	public UserDto getUserByEmail(String email);

	// ### getUserProfileInfo ###
	public UserProfileInfoDto getUserProfileInfoById(Long userId);
	public UserProfileInfoDto getUserProfileInfoByUsername(String username); 
	
	
	// ### 중복체크 ###
	public Boolean checkExists(String checkType, String checkString);

	// ### updateUserInfo ###
	public void updateUserPrivateInfo(UserDto userDto);
	public Boolean updateUserPassword(UserDto userDto);
	public Boolean resetUserPassword(UserDto userDto);

	
	// ### updateUserProfileInfo ###
	public void updateUserProfileInfo(UserProfileInfoDto userProfileInfoDto, MultipartFile profileImgForUpload);
	
	
	// ### deleteUser ###
	public void deleteUser(Long usreId);
	
	// ### follow ###
	public List<UserProfileInfoDto> getFollowerProfileInfoList(Long followeeUserId);
	public List<UserProfileInfoDto> getFolloweeProfileInfoList(Long followerUserId, FollowType followType);
	public void addFollowOrBlock(UserFollowDto userFollowDto);
	public void deleteFollowOrBlock(UserFollowDto userFollowDto);
	
	// ### review ###
	public void addReview(UserReviewDto dto);
	public void deleteReview(Long id);
	public void updateReview(UserReviewDto dto);	
}
