package com.example.tripDuo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@PostMapping("/send")
	public ResponseEntity<String> sendVerificationCode(@RequestBody String phoneNumber) {
		String p_number = phoneNumber.substring(0, phoneNumber.length()-1);
		System.out.println(p_number);
		service.sendVerificationCode(p_number);
		return ResponseEntity.ok("Verification code sent");
	}

	@PostMapping("/verify") // dto로 처리 ? 
	public ResponseEntity<String> verifyPhoneNumber(@RequestBody String str) {
		String phoneNumber = str.substring(0, 11);
		String verificationCode = str.substring(11, str.length() - 1);
				
		boolean isVerified = service.verifyPhoneNumber(phoneNumber, verificationCode);
		if (isVerified) {
			return ResponseEntity.ok("Phone number verified successfully");
		} else {
			return ResponseEntity.badRequest().body("Invalid verification code");
		}
	}
}
