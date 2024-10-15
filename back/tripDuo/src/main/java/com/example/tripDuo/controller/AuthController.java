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

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;
	ObjectMapper objectMapper = new ObjectMapper();
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDto dto) throws Exception {
		return ResponseEntity.ok(authService.login(dto));
	}

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody UserDto userDto) throws Exception {
	    
	    return ResponseEntity.ok(authService.signup(userDto));
	}

	// 회원가입시 사용하는 api - send-code, verify-code
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

	// 기존 사용자가 휴대폰 인증을 하는 api 
	@PostMapping("/phone/forgot-credentials/send-code")
	public ResponseEntity<String> sendVerificationCodeToPhoneForExistingUser(@RequestBody String usernameWithPhoneNumber) throws Exception {

		try {
            JsonNode jsonNode = objectMapper.readTree(usernameWithPhoneNumber);

            String username = jsonNode.get("username").asText();
            String phoneNumber = jsonNode.get("phoneNumber").asText();

            if(authService.sendVerificationCodeToPhoneForExistingUser(username, phoneNumber)) {
    			return ResponseEntity.ok("Verification code is sent");
    		} else {
    			return ResponseEntity.badRequest().body("Invalid phone number");
    		}
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid JSON format.");
        }
		
	}
	
	@PostMapping("/phone/forgot-credentials/verify-code")
	public ResponseEntity<String> verifyCodePhoneForExistingUser(@RequestBody String phoneNumberWithCode) {
		
		try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(phoneNumberWithCode);

            String phoneNumber = jsonNode.get("phoneNumber").asText();
            String verificationCode = jsonNode.get("code").asText();

    		boolean isVerified = authService.verifyEncryptedPhoneNumber(phoneNumber, verificationCode);
    		
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
	
	@PostMapping("/phone/forgot-credentials/find-username")
	public ResponseEntity<String> findUsername(@RequestBody String inputPhoneNumber) {
		String phoneNumber = inputPhoneNumber.substring(0, inputPhoneNumber.length()-1);
		System.out.println(phoneNumber);
		try {
			return ResponseEntity.ok(authService.findUsernameByPhoneNumber(phoneNumber));
		} catch (Exception error) {
			return ResponseEntity.badRequest().body("Invalid phone number");
		}
	}
	
	
	
	// 토큰 발급
	@GetMapping("/google/accessTokenCallback")
	public ResponseEntity<String> googleAccessToken(String code) {
		String googleToken = authService.GoogleAccessToken(code);

		try {
			return ResponseEntity.status(HttpStatus.OK).body(googleToken);
		} catch (Exception error) {
			return ResponseEntity.badRequest().body("invalid google access token");
		}
	}

	@GetMapping("/googleLogin")
	public ResponseEntity<String> googleInfo(@RequestHeader("Authorization") String token) {
		String accessToken = token.replace("Bearer ", "");

		OAuthToken oAuthToken = new OAuthToken();
		oAuthToken.setAccess_token(accessToken);
		String googleinfo = authService.GoogleSignUp(oAuthToken);

		try {
			return ResponseEntity.status(HttpStatus.OK).body(googleinfo);
		} catch (Exception error) {
			return ResponseEntity.badRequest().body("invalid google user");
		}
	}

	// 토큰 발급
	@GetMapping("/kakao/accessTokenCallback")
	public ResponseEntity<String> kakaoAccessToken(String code) {
		String kakaoToken = authService.KakaogetAccessToken(code);

		try {
			return ResponseEntity.status(HttpStatus.OK).body(kakaoToken);
		} catch (Exception error) {
			return ResponseEntity.badRequest().body("invalid kakao access token");
		}
	}

	// 유저 정보 가져오기
	@GetMapping("/kakaoLogin")
	public ResponseEntity<String> kakaoInfo(@RequestHeader("Authorization") String token) {
		String accessToken = token.replace("Bearer ", "");

		OAuthToken oAuthToken = new OAuthToken();
		oAuthToken.setAccess_token(accessToken);
		String kakaoInfo = authService.KakaoSignUp(oAuthToken);

		try {
			return ResponseEntity.status(HttpStatus.OK).body(kakaoInfo);
		} catch (Exception error) {
			return ResponseEntity.badRequest().body("invalid kakao user");
		}
	}

	@PostMapping("/kakaoLogout")
	public ResponseEntity<String> kakaoLogout(@RequestHeader("Authorization") String token, Long kakaoId) {
		String accessToken = token.replace("Bearer ", "");

		OAuthToken oAuthToken = new OAuthToken();
		oAuthToken.setAccess_token(accessToken);
		String kakaoLogout = authService.kakaoLogout(oAuthToken, kakaoId);

		try {
			return ResponseEntity.status(HttpStatus.OK).body(kakaoLogout);
		} catch (Exception error) {
			return ResponseEntity.badRequest().body("invalid google user");
		}
	}
	

}
