package com.example.tripDuo.service;

import com.example.tripDuo.dto.UserDto;

public interface UserService {

	
	// ### getUser ### 
	public UserDto getUserById(int id);
	public UserDto getUserByUsername(String username);
	public UserDto getUserByPhonenum(String phone_num);
	public UserDto getUserByEmail(String email);
	
	// ### updateUser ###
	public boolean updateUser(UserDto dto);
	
	// ### deleteUser ###
	public boolean deleteUser(int id);
	
		
}
