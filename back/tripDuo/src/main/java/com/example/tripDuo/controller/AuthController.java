package com.example.tripDuo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tripDuo.dto.OAuthToken;
import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.service.AuthService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserDto dto) throws Exception {
		return ResponseEntity.ok(authService.login(dto));
	}

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody UserDto dto) throws Exception {
		return ResponseEntity.ok(authService.signup(dto));
	}

	@PostMapping("/phone/send-code")
	public ResponseEntity<String> sendVerificationCodeToPhone(@RequestBody String inputPhoneNumber) {
		String phoneNumber = inputPhoneNumber.substring(0, inputPhoneNumber.length()-1);
		
		if(authService.sendVerificationCode(phoneNumber)) {
			return ResponseEntity.ok("Verification code is sent");
		} else {
			return ResponseEntity.badRequest().body("Invalid phone number");
		}
	}

	@PostMapping("/phone/verify-code")
	public ResponseEntity<String> verifyPhoneCode(@RequestBody String phoneNumberWithCode) {
		
		try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(phoneNumberWithCode);

            String phoneNumber = jsonNode.get("phoneNumber").asText();
            String verificationCode = jsonNode.get("code").asText();

    		boolean isVerified = authService.verifyPhoneNumber(phoneNumber, verificationCode);
    		
    		if (isVerified) {
    			return ResponseEntity.ok("Phone number verified successfully");
    		} else {
    			return ResponseEntity.badRequest().body("Invalid verification code");
    		}
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid JSON format.");
        }
	}

	// 토큰 발급 
	@GetMapping("/kakao/accessTokenCallback")
	public ResponseEntity<String> kakaoAccessToken(String code, HttpServletRequest request, OAuthToken oAuthToken) throws Exception {
		String kakaoToken = authService.KakaogetAccessToken(request.getParameter("code"));
		return ResponseEntity.status(HttpStatus.OK).body(kakaoToken);
		
	}
	
	// 유저 정보 가져오기
	@GetMapping("/kakaoLogin")
	public ResponseEntity<String> kakaoInfo(@RequestHeader("Authorization") String token) {
		String accessToken = token.replace("Bearer ", "");
	    System.out.println("Extracted Access Token: " + accessToken); // 로그로 확인

		OAuthToken oAuthToken = new OAuthToken();
	    oAuthToken.setAccess_token(accessToken);
	    
	    String kakaoInfo = authService.KakaoSignUp(oAuthToken);
	 
	    return ResponseEntity.status(HttpStatus.OK).body(kakaoInfo);
	}
	
	@PostMapping("/kakaoLogout")
	public ResponseEntity<String> kakaoLogout(@RequestHeader("Authorization") String authHeader, Long  kakaoId) {
		String accessToken = authHeader.replace("Bearer ", "");
		OAuthToken oAuthToken = new OAuthToken();
	    oAuthToken.setAccess_token(accessToken);

		String kakaoLogout = authService.kakaoLogout(oAuthToken, kakaoId);
		
		return ResponseEntity.status(HttpStatus.OK).body(kakaoLogout);
	}
	

}
