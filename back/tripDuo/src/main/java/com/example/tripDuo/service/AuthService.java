package com.example.tripDuo.service;

import java.util.Map;

import com.example.tripDuo.dto.OAuthToken;
import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.entity.User;

public interface AuthService {
	
	public Map<String, Object> login(UserDto dto) throws Exception;
	public String signup(UserDto userDto) throws Exception;
	
    public String sendVerificationCode(String phoneNumber);
    public boolean verifyPhoneNumber(String phoneNumber, String verificationCode);
    
    public boolean sendVerificationCodeToPhoneForExistingUser(String username,  String phoneNumber) throws Exception;
    public boolean verifyEncryptedPhoneNumber(String phoneNumber, String verificationCode);
    
    public String findUsernameByPhoneNumber(String phoneNumber) throws Exception;
    
    public User KakaoFindId(String username);
    public String KakaogetAccessToken(String code);
    public Map<String, Object> KakaoSignUp(OAuthToken kakaoToken) throws Exception;
    public String kakaoLogout(OAuthToken oAuthToken, Long kakaoId);
    
    public String GoogleAccessToken(String code);
    public Map<String, Object> GoogleSignUp(OAuthToken googleToken) throws Exception;
    

}
