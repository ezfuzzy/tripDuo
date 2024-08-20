package com.example.tripDuo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	@Autowired
	private AuthService service;
	
	@PostMapping("/login")
	public String login(@RequestBody UserDto dto) throws Exception {
		return service.login(dto);
	}
	
	@PostMapping("/signup")
	public String signup(@RequestBody UserDto dto) {
		return service.signup(dto);
	}
}
