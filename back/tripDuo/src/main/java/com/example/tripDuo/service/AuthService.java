package com.example.tripDuo.service;

import com.example.tripDuo.dto.OAuthToken;
import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.dto.UserProfileInfoDto;
import com.example.tripDuo.entity.User;

public interface AuthService {
	
	public String login(UserDto dto) throws Exception;
	public String signup(UserDto userDto, UserProfileInfoDto userProfileInfoDto);
	
    public boolean sendVerificationCode(String phoneNumber);
    public boolean verifyPhoneNumber(String phoneNumber, String verificationCode);
    
    public String GoogleAccessToken(String code);
    public String GoogleSignUp(OAuthToken googleToken)  ;
    public User KakaoFindId(String username);
    public String KakaoSignUp(OAuthToken kakaoToken);
    public String KakaogetAccessToken(String code);
    public String kakaoLogout(OAuthToken oAuthToken, Long kakaoId);

}
