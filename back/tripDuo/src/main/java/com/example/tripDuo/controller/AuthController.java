package com.example.tripDuo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tripDuo.dto.OAuthToken;
import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import kong.unirest.json.JSONObject;

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

	@PostMapping("/verify")
	public ResponseEntity<String> verifyPhoneNumber(@RequestBody String phone_number_with_code) {
        JSONObject jsonObject = new kong.unirest.json.JSONObject(phone_number_with_code);

        String phone_number = jsonObject.getString("phone_number");
        String verification_code = jsonObject.getString("code");
				
		boolean isVerified = service.verifyPhoneNumber(phone_number, verification_code);
		if (isVerified) {
			return ResponseEntity.ok("Phone number verified successfully");
		} else {
			return ResponseEntity.badRequest().body("Invalid verification code");
		}
	}
	
	@GetMapping("/test")
	public String testUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	// 토큰 발급 
	@GetMapping("/accesstokencallback")
	public ResponseEntity<String> kakaoAccessToken(String code, HttpServletRequest request, OAuthToken oAuthToken) throws Exception {
		String kakaoToken = service.KakaogetAccessToken(request.getParameter("code"));
		return ResponseEntity.status(HttpStatus.OK).body(kakaoToken);
		
	}
	
	// 유저 정보 가져오기
	@GetMapping("/kakaologin")
	public ResponseEntity<String> kakaoInfo(@RequestHeader("Authorization") String token) {
		String accessToken = token.replace("Bearer ", "");
	    System.out.println("Extracted Access Token: " + accessToken); // 로그로 확인

		OAuthToken oAuthToken = new OAuthToken();
	    oAuthToken.setAccess_token(accessToken);
	    
	    String kakaoInfo = service.KakaoSignUp(oAuthToken);
	 
	    return ResponseEntity.status(HttpStatus.OK).body(kakaoInfo);
	}
	
	@PostMapping("/kakaologout")
	public ResponseEntity<String> kakaoLogout(@RequestHeader("Authorization") String authHeader, Long  kakaoId) {
		String accessToken = authHeader.replace("Bearer ", "");
		OAuthToken oAuthToken = new OAuthToken();
	    oAuthToken.setAccess_token(accessToken);

		String kakaoLogout=service.kakaoLogout(oAuthToken, kakaoId);
		
		return ResponseEntity.status(HttpStatus.OK).body(kakaoLogout);
	}
	
}
