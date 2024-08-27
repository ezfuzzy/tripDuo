package com.example.tripDuo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.repository.UserRepository;
import com.example.tripDuo.util.JwtUtil;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

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

	@Value("${mailgun.key}")
	private String MAIL_API_KEY;

	@Override
	public String login(UserDto dto) throws Exception {

		System.out.println(dto.getUsername() + " " + dto.getPassword());

		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dto.getUsername(),
					dto.getPassword());

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

		System.out.println("[AuthServiceImpl - sendVerificationCode] verificationCode : " + verificationCode);

		try {
			sendVerificationCodeToEmail(verificationCode);
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean verifyPhoneNumber(String phoneNumber, String verificationCode) {
		// 4. 사용자가 제출한 인증번호 검증
		return phoneNumberVerificationService.verifyCode(phoneNumber, verificationCode);
	}

	public JsonNode sendVerificationCodeToEmail(String code) throws UnirestException {
		
		// 일단 무료계정에선 "to" 부분을 email api site에서 인증받은 계정에만 전송할 수 있는 것 같음 -> 나중에 plan 업그레이드 예정 
		
		HttpResponse<JsonNode> response = Unirest
				.post("https://api.mailgun.net/v3/sandboxe612253c42634473b260f15ed6e389ad.mailgun.org/messages")
				.basicAuth("api", MAIL_API_KEY)
				.queryString("from", "Excited User <USER@sandboxe612253c42634473b260f15ed6e389ad.mailgun.org>")
				.queryString("to", "ezfuzzy062@gmail.com") // email 값을 변경해야함
				.queryString("subject", "tripDuo에 가입하신걸 환영합니다 or tripDuo 메일 인증코드 도착했습니다")
				.queryString("text", "이런 저런 양식 ... \n 메일 인증코드는 : " + code + " 입니다.")
				.asJson();
		
		return response.getBody();
	}
}