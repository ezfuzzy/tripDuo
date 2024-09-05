package com.example.tripDuo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.tripDuo.dto.UserDto;

public interface UserService {

	
	// ### getUser ### 
	public List<UserDto> getUserList();
	
	public UserDto getUserById(Long id);
	public UserDto getUserByUsername(String username);
	public UserDto getUserByPhonenum(String phone_num);
	public UserDto getUserByEmail(String email);
	
	// ### updateUser ###
	public Boolean updateUser(UserDto dto, MultipartFile profileImgForUpload);
	
	// ### deleteUser ###
	public void deleteUser(Long id);
	
		
}
