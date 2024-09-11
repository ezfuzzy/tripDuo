package com.example.tripDuo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.dto.UserFollowDto;
import com.example.tripDuo.dto.UserReviewDto;

public interface UserService {

	
	// ### getUser ### 
	public List<UserDto> getUserList();
	
	public UserDto getUserById(Long id);
	public UserDto getUserByUsername(String username);
	public UserDto getUserByPhoneNumber(String phone_num);
	public UserDto getUserByEmail(String email);

	// ### 중복체크 ###
	public Boolean checkExists(String checkType, String checkString);

	// ### updateUserInfo ###
	public void updateUserInfo(UserDto dto, MultipartFile profileImgForUpload);
	public Boolean updateUserPassword(UserDto dto);
	public Boolean resetUserPassword(UserDto dto);

	// ### deleteUser ###
	public void deleteUser(Long id);
	
	// ### follow ###
	public void addFollow(UserFollowDto dto);
	
	// ### review ###
	public void addReview(UserReviewDto dto);
	public void deleteReview(Long id);
	public void editReview(UserReviewDto dto);
}
