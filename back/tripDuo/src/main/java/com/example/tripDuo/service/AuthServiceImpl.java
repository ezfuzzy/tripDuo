package com.example.tripDuo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.repository.UserRepository;
import com.example.tripDuo.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserRepository repo;

	

	
	@Override
	public String login(UserDto dto) throws Exception {
		
		System.out.println(dto.getUsername() + " " + dto.getPassword());

		try {
			UsernamePasswordAuthenticationToken authToken = 
					new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
			
			authManager.authenticate(authToken);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("아이디 혹은 비밀번호가 틀려요!");
		}
		String token = jwtUtil.generateToken(dto.getUsername());
		return "Bearer+" + token;
	}

	@Override
	public String signup(UserDto dto) {
	    
		// 회원가입 페이지에서 user, manager, admin 설정 or admin dashboard page에서 관리
		dto.setRole("USER"); 
		String encodedPwd = encoder.encode(dto.getPassword());
	    dto.setPassword(encodedPwd);
	    
	    repo.save(User.toEntity(dto));
	    
		return "회원가입 완료";
	}

	
	// ### 휴대폰 인증 ###
	
    private final PhoneNumberVerificationService phoneNumberVerificationService;

    public AuthServiceImpl(PhoneNumberVerificationService phoneNumberVerificationService) {
        this.phoneNumberVerificationService = phoneNumberVerificationService;
    }
    
    @Override
	public void sendVerificationCode(String phoneNumber) {
        // 1. 인증번호 생성
        String verificationCode = phoneNumberVerificationService.generateVerificationCode();

        // 2. 인증번호와 휴대폰 번호를 저장
        phoneNumberVerificationService.storeVerificationCode(phoneNumber, verificationCode);
        
        
        System.out.println("phoneNumber : " + phoneNumber);
        System.out.println("verificationCode : " + verificationCode);
        
        // 3. SMS 또는 다른 채널을 통해 인증번호를 사용자에게 전송
        // 예시: smsService.sendSMS(phoneNumber, verificationCode);
    }

    @Override
    public boolean verifyPhoneNumber(String phoneNumber, String verificationCode) {
        // 4. 사용자가 제출한 인증번호 검증
        return phoneNumberVerificationService.verifyCode(phoneNumber, verificationCode);
    }
}
