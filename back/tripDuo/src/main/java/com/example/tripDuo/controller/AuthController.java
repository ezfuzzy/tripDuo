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
    public ResponseEntity<String> sendVerificationCode(@RequestParam String phoneNumber) {
    	System.out.println(phoneNumber);
        //service.sendVerificationCode(phoneNumber);
        return ResponseEntity.ok("Verification code sent");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPhoneNumber(@RequestParam String phoneNumber,
                                                    @RequestParam String verificationCode) {
        boolean isVerified = service.verifyPhoneNumber(phoneNumber, verificationCode);
        if (isVerified) {
            return ResponseEntity.ok("Phone number verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid verification code");
        }
    }
}
