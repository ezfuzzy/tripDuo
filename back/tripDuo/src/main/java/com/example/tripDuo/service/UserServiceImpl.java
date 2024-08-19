package com.example.tripDuo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository repo;

	@Override
	public UserDto getDataByUsername(String username) {
		// TODO Auto-generated method stub
		//return UserDto.toDto(repo.findByUsername(username).get());
		return null;
	}
	
	
}
