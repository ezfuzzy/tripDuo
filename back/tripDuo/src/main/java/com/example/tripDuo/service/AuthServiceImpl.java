package com.example.tripDuo.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.tripDuo.dto.KakaoProfile;
import com.example.tripDuo.dto.OAuthToken;
import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.entity.User;
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
	private final UserRepository repo;
	private final PhoneNumberVerificationService phoneNumberVerificationService;

	@Value("${mailgun.key}")
	private String MAIL_API_KEY;

	@Value("${coolsms.send_number}")
	private String send_number;
	
	@Value("${coolsms.key}")
	private String COOLSMS_KEY;
	
	@Value("${coolsms.secret}")
	private String COOLSMS_SECRET;
	
	@Value("${kakaopassword.key}")
	private String kakaopassword;
	
	@Value("${kakao.client_id}")
	private String client_id;
	
	@Value("${kakao.redirect_uri}")
	private String redirect_url;

	public AuthServiceImpl(JwtUtil jwtUtil, AuthenticationManager authManager, PasswordEncoder encoder, UserRepository repo, PhoneNumberVerificationService phoneNumberVerificationService) {
		this.jwtUtil = jwtUtil;
		this.authManager = authManager;
		this.encoder = encoder;
		this.repo = repo;
		this.phoneNumberVerificationService = phoneNumberVerificationService;
	}

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
				User user  = repo.findByUsername(username);
				return user;
			}

			// 카카오 로그인 및 가입
			@Override
			public String KakaoSignUp(OAuthToken kakaoToken) {
				// 1. Kakao API를 통해 사용자 정보 요청
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
				        String.class // String 타입으로 응답 데이터를 받겠다.
				);
				
				System.out.println("유저정보 : " + response2.getBody());

				// 2. 응답된 JSON을 KakaoProfile 객체로 변환
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
				
					 // 3. 유저 정보 생성
					User kakaoUser=User.builder()
							.username(kakaoProfile.getId()+"_"+kakaoProfile.getKakao_account().getEmail())
							.password(encoder.encode(kakaopassword))
							.email(kakaoProfile.getKakao_account().getEmail())
							.build();
					
					// 4. 데이터베이스에서 유저 조회
					User originUser = repo.findByUsername(kakaoUser.getUsername());
						if (originUser != null) {
					        System.out.println("이미 존재하는 유저입니다.");
					    } else {
					        // 5. 유저가 없을 경우 저장
					        repo.save(kakaoUser);
					        System.out.println("새로운 유저가 저장되었습니다.");
					    }
						
						 // 6. 로그인 처리	 
						Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), kakaopassword));
						SecurityContextHolder.getContext().setAuthentication(authentication);
						
						// 7. 카카오 유저 정보를 JSON으로 반환
						JSONObject kakaoInfo2 = new JSONObject();
						kakaoInfo2.put("id", kakaoProfile.id);
						kakaoInfo2.put("nickname", kakaoProfile.getKakao_account().getProfile().getNickname());
			            kakaoInfo2.put("profile_image", kakaoProfile.getKakao_account().getProfile().getProfile_image_url());
			            kakaoInfo2.put("email", kakaoProfile.getKakao_account().getEmail());
			            kakaoInfo2.put("kakaoToken", kakaoToken.getAccess_token());
			            kakaoInfo2.put("kakaoRefreshToken", kakaoToken.getRefresh_token());
			            
			            System.out.println("kakaoInfo2: " + kakaoInfo2.toString());  // 보기 좋게 출력
			        return kakaoInfo2.toString();
			        		
			}
			// 카카오 액세스 토큰 발급 
			@Override
			public String KakaogetAccessToken(String code) {
				RestTemplate rt = new RestTemplate();
				
				// HttpHeader 오브젝트 생성
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
				
				// HttpBody 오브젝트 생성
				MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
				params.add("grant_type", "authorization_code");
				params.add("client_id", client_id);
				params.add("redirect_url", redirect_url);
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
				System.out.println("11");
				 // RestTemplate를 사용해 로그아웃 요청
			    RestTemplate rt = new RestTemplate();
			    HttpHeaders headers = new HttpHeaders();
			    headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
			    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


			 // HttpBody 오브젝트 생성
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