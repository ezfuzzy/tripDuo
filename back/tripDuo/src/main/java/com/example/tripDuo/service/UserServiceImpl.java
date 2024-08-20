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
	public UserDto getUserById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDto getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDto getUserByPhonenum(String phone_num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateUser(UserDto dto) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUser(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}
