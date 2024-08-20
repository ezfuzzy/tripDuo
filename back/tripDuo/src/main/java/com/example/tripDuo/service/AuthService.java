package com.example.tripDuo.service;

import com.example.tripDuo.dto.UserDto;

public interface AuthService {
	
	public String login(UserDto dto) throws Exception;
	public String signup(UserDto dto);
}
