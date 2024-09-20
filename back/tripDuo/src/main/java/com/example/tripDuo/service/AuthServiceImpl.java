package com.example.tripDuo.service;

import java.util.regex.Pattern;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.tripDuo.dto.GoogleProfile;
import com.example.tripDuo.dto.KakaoProfile;
import com.example.tripDuo.dto.OAuthToken;
import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.dto.UserProfileInfoDto;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.entity.UserProfileInfo;
import com.example.tripDuo.enums.UserRole;
import com.example.tripDuo.repository.UserProfileInfoRepository;
import com.example.tripDuo.repository.UserRepository;
import com.example.tripDuo.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

@Service
public class AuthServiceImpl implements AuthService {

	private final JwtUtil jwtUtil;
	private final AuthenticationManager authManager;
	private final PasswordEncoder encoder;
	private final UserRepository userRepo;
	private final UserProfileInfoRepository userProfileInfoRepo;
	private final PhoneNumberVerificationService phoneNumberVerificationService;

	@Value("${mailgun.key}")
	private String MAIL_API_KEY;

	@Value("${coolsms.send_number}")
	private String send_number;
	
	@Value("${coolsms.key}")
	private String COOLSMS_KEY;
	
	@Value("${coolsms.secret}")
	private String COOLSMS_SECRET;
	
	@Value("${google.client.key}")
	private String GOOGLE_LOGIN_KEY;
	
	@Value("${google.client.secret}")
	private String GOOGLE_LOGIN_SECRET;
			
	@Value("${google.redirect.uri}")
	private String GOOGLE_REDIRECT_URI;
	
	@Value("${oauthpassword}")
	private String OAUTHPASSWORD;
	
	@Value("${kakao.client_key}")
	private String KAKAO_LOGIN_KEY;
	
	@Value("${kakao.redirect.uri}")
	private String KAKAO_REDIRECT_URI;

	public AuthServiceImpl(JwtUtil jwtUtil, AuthenticationManager authManager, 
			PasswordEncoder encoder, UserRepository userRepo, 
			UserProfileInfoRepository userProfileInfoRepo,
			PhoneNumberVerificationService phoneNumberVerificationService) {
		this.jwtUtil = jwtUtil;
		this.authManager = authManager;
		this.encoder = encoder;
		this.userRepo = userRepo;
		this.userProfileInfoRepo = userProfileInfoRepo;
		this.phoneNumberVerificationService = phoneNumberVerificationService;
	}

	@Override
	public String login(UserDto userDto) throws Exception {

		System.out.println(userDto.getUsername() + " " + userDto.getPassword());

		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDto.getUsername(),
					userDto.getPassword());

			authManager.authenticate(authToken);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail to login"; // id or password is wrong
		}
		String token = jwtUtil.generateToken(userDto.getUsername());
		return "Bearer+" + token;
	}

	@Override
	public String signup(UserDto userDto) {

		// ### username, nickname, password 유효성 체크 ###
		
		String usernamePattern = "^[a-z0-9]{6,16}$";
        String nicknamePattern = "^[가-힣a-zA-Z0-9]{4,16}$";
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,22}$";

		if(!Pattern.matches(usernamePattern, userDto.getUsername()) || 
				!Pattern.matches(nicknamePattern, userDto.getNickname()) ||
				!Pattern.matches(passwordPattern, userDto.getPassword())) {
			return "유효성 검사 탈락";
		}
		
		userDto.setRole(UserRole.USER);
		String encodedPwd = encoder.encode(userDto.getPassword());
		userDto.setPassword(encodedPwd);

		
		User savedUser = userRepo.save(User.toEntity(userDto));
		
		userProfileInfoRepo.save(UserProfileInfo.toEntity(
				UserProfileInfoDto.builder().nickname(userDto.getNickname()).build(), savedUser));
		
		String token = jwtUtil.generateToken(userDto.getUsername());

		return "Bearer+" + token;
	}

	// ### 휴대폰 인증 ###

	@Override
	public boolean sendVerificationCode(String phoneNumber) {
		// 1. 인증번호 생성
		String verificationCode = phoneNumberVerificationService.generateVerificationCode();

		// 2. 인증번호와 휴대폰 번호를 저장
		phoneNumberVerificationService.storeVerificationCode(phoneNumber, verificationCode);

		// test code -
		System.out.println("[AuthServiceImpl - sendVerificationCode] verificationCode : " + verificationCode);

//		// ### 문자 전송 api ### 
//		DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(COOLSMS_KEY, COOLSMS_SECRET, "https://api.coolsms.co.kr");
//		
//		
//		Message message = new Message();
//		message.setFrom(send_number);
//		message.setTo(phoneNumber);
//		message.setText("[tripDuo] 인증코드 : " + verificationCode);
//
//		try {
//		  messageService.send(message);
//		} catch (NurigoMessageNotReceivedException exception) {
//		  System.out.println(exception.getFailedMessageList());
//		  System.out.println(exception.getMessage());
//		} catch (Exception exception) {
//		  System.out.println(exception.getMessage());
//		}

		return true;
//		try {
//			sendVerificationCodeToEmail(verificationCode);
//		} catch (UnirestException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public boolean verifyPhoneNumber(String phoneNumber, String verificationCode) {
		// 4. 사용자가 제출한 인증번호 검증
		return phoneNumberVerificationService.verifyCode(phoneNumber, verificationCode);
	}

	// 나중에 환영 메일 발송으로 활용할듯?
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
	
			// 카카오 회원 찾기
			@Override
			public User KakaoFindId(String username) {
				User user  = userRepo.findByUsername(username);
				return user;
			}
			
			

			@Override
			public String GoogleAccessToken(String code) {
				RestTemplate rt = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-type", "application/x-www-form-urlencoded");
				
				MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			        params.add("code", code);
			        params.add("client_id", GOOGLE_LOGIN_KEY);
			        params.add("client_secret", GOOGLE_LOGIN_SECRET);
			        params.add("redirect_uri", GOOGLE_REDIRECT_URI);
			        params.add("grant_type", "authorization_code");
				
				HttpEntity<MultiValueMap<String, String>> googleTokenRequest = 
						new HttpEntity<>(params, headers);
				System.out.println(params);
				
				ResponseEntity<String> response = rt.exchange(
						"https://oauth2.googleapis.com/token",
						HttpMethod.POST,
						googleTokenRequest,
						String.class
				);

				System.out.println(response.getBody());
				
				ObjectMapper objectMapper = new ObjectMapper();
				OAuthToken oauthToken = null;

				try {
					oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				System.out.println("구글 엑세스 토큰 : "+oauthToken.getAccess_token());
				return "Bearer "+oauthToken.getAccess_token();
			}

			@Override
			public String GoogleSignUp(OAuthToken googleToken) {
				RestTemplate  rt2 = new RestTemplate();
				HttpHeaders headers2 = new HttpHeaders();
				headers2.add("Authorization", "Bearer " + googleToken.getAccess_token());
				headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); 

				HttpEntity<MultiValueMap<String, String>> googleProfileRequest = 
						new HttpEntity<>(headers2);

				ResponseEntity<String> response2 = rt2.exchange(
						"https://www.googleapis.com/userinfo/v2/me",
						HttpMethod.GET,
						googleProfileRequest,
				        String.class
				);
				
				System.out.println("유저정보 : " + response2.getBody());

				ObjectMapper objectMapper2 = new ObjectMapper();
				objectMapper2.registerModule(new JavaTimeModule());
				objectMapper2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				objectMapper2.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE); 
				GoogleProfile googleProfile = null;
					try {
						googleProfile = objectMapper2.readValue(response2.getBody(), GoogleProfile.class);
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				
				UserDto user=new UserDto();
				user.setPhoneNumber(googleProfile.getEmail());
				User googleUser=User.builder()
						.username("google_"+googleProfile.getEmail())
						.password(encoder.encode(OAUTHPASSWORD))
						.email(googleProfile.getEmail())
						.phoneNumber(user.getPhoneNumber())
						.build();
					
				User originUser = userRepo.findByUsername(googleUser.getUsername());
					if (originUser != null) {
				        System.out.println("이미 존재하는 유저입니다.");
				    } else {
				    	userRepo.save(googleUser);
				        System.out.println("새로운 유저가 저장되었습니다.");
				    }
						
					Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(googleUser.getUsername(), OAUTHPASSWORD));
					SecurityContextHolder.getContext().setAuthentication(authentication);
						
					JSONObject googleInfo2 = new JSONObject();
					googleInfo2.put("id", googleProfile.getId());
					googleInfo2.put("name", googleProfile.getName());
					googleInfo2.put("nickname", googleProfile.getGivenName());
					googleInfo2.put("picture", googleProfile.getPicture());
					googleInfo2.put("email", googleProfile.getEmail());
					googleInfo2.put("googleToken",googleToken.getAccess_token());
					googleInfo2.put("kakaoRefreshToken", googleToken.getRefresh_token());
					googleInfo2.put("phonenum", user.getPhoneNumber());
					
			        System.out.println("googleInfo2: " + googleInfo2.toString());  // 보기 좋게 출력
			                 
			        return googleInfo2.toString();
			}
			
			@Override
			public String KakaoSignUp(OAuthToken kakaoToken) {
				RestTemplate  rt2 = new RestTemplate();
				HttpHeaders headers2 = new HttpHeaders();
				headers2.add("Authorization", "Bearer " + kakaoToken.getAccess_token());
				headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // 내가 지금 전송할 body data 가

				HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = 
						new HttpEntity<>(headers2);

				ResponseEntity<String> response2 = rt2.exchange(
						"https://kapi.kakao.com/v2/user/me",
						HttpMethod.POST,
						kakaoProfileRequest,
				        String.class 
				);
				
				System.out.println("유저정보 : " + response2.getBody());

				ObjectMapper objectMapper2 = new ObjectMapper();
				objectMapper2.registerModule(new JavaTimeModule());
				objectMapper2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				objectMapper2.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE); 
				KakaoProfile kakaoProfile = null;
					try {
						kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				
				UserDto user=new UserDto();
				user.setPhoneNumber(kakaoProfile.getKakao_account().getEmail());
				User kakaoUser=User.builder()
						.username("kakao_"+kakaoProfile.getKakao_account().getEmail())
						.password(encoder.encode(OAUTHPASSWORD))
						.email(kakaoProfile.getKakao_account().getEmail())
						.phoneNumber(user.getPhoneNumber())

						.build();
					
				User originUser = userRepo.findByUsername(kakaoUser.getUsername());
					if (originUser != null) {
				        System.out.println("이미 존재하는 유저입니다.");
				    } else {
				        // 5. 유저가 없을 경우 저장
				    	userRepo.save(kakaoUser);
				        System.out.println("새로운 유저가 저장되었습니다.");
				    }
						
				Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), OAUTHPASSWORD));
				SecurityContextHolder.getContext().setAuthentication(authentication);
						
				JSONObject kakaoInfo2 = new JSONObject();
				kakaoInfo2.put("id", kakaoProfile.id);
				kakaoInfo2.put("nickname", kakaoProfile.getKakao_account().getProfile().getNickname());
		        kakaoInfo2.put("profile_image", kakaoProfile.getKakao_account().getProfile().getProfile_image_url());
		        kakaoInfo2.put("email", kakaoProfile.getKakao_account().getEmail());
		        kakaoInfo2.put("kakaoToken", kakaoToken.getAccess_token());
		        kakaoInfo2.put("kakaoRefreshToken", kakaoToken.getRefresh_token());
		        kakaoInfo2.put("phonenum", user.getPhoneNumber());

		        System.out.println("kakaoInfo2: " + kakaoInfo2.toString());  // 보기 좋게 출력

		        return kakaoInfo2.toString();
			}

			@Override
			public String KakaogetAccessToken(String code) {
				RestTemplate rt = new RestTemplate();
				
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
				
				// HttpBody 오브젝트 생성
				MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
				params.add("grant_type", "authorization_code");
				params.add("client_id", KAKAO_LOGIN_KEY);
				params.add("redirect_url", KAKAO_REDIRECT_URI);
				params.add("code", code);
				
				// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
				HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = 
						new HttpEntity<>(params, headers);
				
				// Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
				ResponseEntity<String> response = rt.exchange(
						"https://kauth.kakao.com/oauth/token",
						HttpMethod.POST,
						kakaoTokenRequest,
						String.class
				);
				// Gson, Json Simple, ObjectMapper
				ObjectMapper objectMapper = new ObjectMapper();
				OAuthToken oauthToken = null;
				try {
					oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				System.out.println("카카오 엑세스 토큰 : "+oauthToken.getAccess_token());
				RestTemplate rt2 = new RestTemplate();
				HttpHeaders headers2 = new HttpHeaders();
				headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
				headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); 

				// HttpHeader 와 HttpBody를 하나의 오브젝트에 담기				
				HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = 
						new HttpEntity<>(headers2);

				// Http 요청하기 - Post 방식으로 - 그리고 Response 변수의 응답 받음.
				ResponseEntity<String> response2 = rt2.exchange(
						"https://kapi.kakao.com/v2/user/me",
						HttpMethod.POST,
						kakaoProfileRequest,
				        String.class // String 타입으로 응답 데이터를 받겠다.
				);
				//return KakaoSignUp(oauthToken);
				//return "Access_Token : Bearer+" +oauthToken.getAccess_token()+" Reflash_Token : "+oauthToken.getRefresh_token();
				return "Bearer "+oauthToken.getAccess_token();
			}
			
			// 카카오 로그아웃
			@Override
			public String kakaoLogout(OAuthToken oAuthToken,  Long kakaoId) {
			    RestTemplate rt = new RestTemplate();
			    HttpHeaders headers = new HttpHeaders();
			    headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
			    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


	 			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	 			params.add("target_id_type", "user_id");
	 			params.add("target_id",  kakaoId.toString());
			 			
			    HttpEntity<MultiValueMap<String, String>> kakaoLogoutRequest = 
			    		new HttpEntity<>(headers);
			    // 로그아웃 요청 (POST)
			    ResponseEntity<String> response = rt.exchange(
			        "https://kapi.kakao.com/v1/user/logout",
			        HttpMethod.POST,
			        kakaoLogoutRequest,
			        String.class
			    );
			    System.out.println(response.getBody());
			    // 응답 확인
			    if (response.getStatusCode().is2xxSuccessful()) {
			        System.out.println("카카오 로그아웃 성공");
			        return "카카오 로그아웃 성공";
			    } else {
			        System.out.println("카카오 로그아웃 실패");
			        return "카카오 로그아웃 실패";
			    }
			  }


			

}