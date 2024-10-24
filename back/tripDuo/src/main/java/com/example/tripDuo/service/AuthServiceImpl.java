package com.example.tripDuo.service;

import java.util.Map;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.tripDuo.dto.GoogleProfile;
import com.example.tripDuo.dto.KakaoProfile;
import com.example.tripDuo.dto.OAuthToken;
import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.dto.UserProfileInfoDto;
import com.example.tripDuo.entity.Oauth;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.entity.UserProfileInfo;
import com.example.tripDuo.entity.UserTripInfo;
import com.example.tripDuo.enums.AccountStatus;
import com.example.tripDuo.enums.UserRole;
import com.example.tripDuo.enums.VerificationStatus;
import com.example.tripDuo.repository.OauthRepository;
import com.example.tripDuo.repository.UserProfileInfoRepository;
import com.example.tripDuo.repository.UserRepository;
import com.example.tripDuo.repository.UserTripInfoRepository;
import com.example.tripDuo.util.EncryptionUtil;
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
	private final UserTripInfoRepository userTripInfoRepo;
	private final OauthRepository oauthRepo;
	private final S3Service s3Service;
	
	private final PhoneNumberVerificationService phoneNumberVerificationService;
	private final EncryptionUtil encryptionUtil;
	
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
	
	@Value("${cloud.aws.cloudfront.profile_picture_url}")
	private String PROFILE_PICTURE_CLOUDFRONT_URL;
	
	public AuthServiceImpl(JwtUtil jwtUtil, AuthenticationManager authManager, 
			PasswordEncoder encoder, UserRepository userRepo, 
			UserProfileInfoRepository userProfileInfoRepo,
			UserTripInfoRepository userTripInfoRepo,
			OauthRepository oauthRepo,
			S3Service s3Service,
			PhoneNumberVerificationService phoneNumberVerificationService,
			EncryptionUtil encryptionUtil) {
		this.jwtUtil = jwtUtil;
		this.authManager = authManager;
		this.encoder = encoder;
		this.userRepo = userRepo;
		this.userProfileInfoRepo = userProfileInfoRepo;
		this.userTripInfoRepo = userTripInfoRepo;
		this.oauthRepo = oauthRepo;
		this.s3Service = s3Service;
		this.phoneNumberVerificationService = phoneNumberVerificationService;
		this.encryptionUtil = encryptionUtil;
	}

	@Override
	public Map<String, Object> login(UserDto userDto) throws Exception {

		UserProfileInfoDto foundUserProfileInfoDto = null;
		
		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDto.getUsername(),
					userDto.getPassword());

			authManager.authenticate(authToken);
			User foundUser = userRepo.findByUsername(userDto.getUsername());
			foundUserProfileInfoDto = UserProfileInfoDto.toDto(userProfileInfoRepo.findById(foundUser.getId()).get(), PROFILE_PICTURE_CLOUDFRONT_URL);
			
			if(foundUser.getAccountStatus() != AccountStatus.ACTIVE) {
				if(foundUser.getDeletedAt() != null) {
					return Map.of(
								"message", "아이디 또는 비밀번호가 틀립니다"
							);
				} else {
					return Map.of(
							"message", "정지되었거나 비활성화된 계정입니다"
						);
				}
					
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Map.of(
					"message", "아이디 또는 비밀번호가 틀립니다"
				);
		}
		
		String token = jwtUtil.generateToken(userDto.getUsername());
		
		return Map.of(
					"token", "Bearer+" + token,
					"userProfileInfo", foundUserProfileInfoDto
				);
	}

	@Override
	@Transactional
	public String signup(UserDto userDto) throws Exception {

		// ### username, nickname, password 유효성 체크 ###
		String usernamePattern = "^[a-z0-9]{6,16}$";
        String nicknamePattern = "^(?=.*[가-힣])(?!.*[^a-zA-Z가-힣0-9])[a-zA-Z가-힣0-9]{2,8}$|^(?!.*[가-힣])(?!.*[^a-zA-Z0-9])[a-zA-Z0-9]{4,16}$";
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,22}$";

        if(!Pattern.matches(usernamePattern, userDto.getUsername()) || 
				!Pattern.matches(nicknamePattern, userDto.getNickname()) ||
				!Pattern.matches(passwordPattern, userDto.getPassword())) {
			return "유효성 검사 탈락";
		}

		userDto.setRole(UserRole.USER);
		userDto.setAccountStatus(AccountStatus.ACTIVE);
		userDto.setVerificationStatus(VerificationStatus.VERIFIED);
		String encodedPwd = encoder.encode(userDto.getPassword());
		userDto.setPassword(encodedPwd);

		// 비밀번호 암호화
        String encryptedPhoneNumber = encryptionUtil.encrypt(userDto.getEncryptedPhoneNumber());
		userDto.setEncryptedPhoneNumber(encryptedPhoneNumber);
        
		User savedUser = userRepo.save(User.toEntity(userDto));
		
		UserProfileInfoDto upiDto = UserProfileInfoDto.builder()
												.nickname(userDto.getNickname())
												.age(userDto.getAge())
												.gender(userDto.getGender())
												.build();
		
		userProfileInfoRepo.save(UserProfileInfo.toEntity(upiDto, savedUser));
		
		userTripInfoRepo.save(UserTripInfo.builder().userId(savedUser.getId()).build());

		String token = jwtUtil.generateToken(userDto.getUsername());

		return "Bearer+" + token;
	}
	
	// ### 휴대폰 인증 ###

	@Override
	public String sendVerificationCode(String phoneNumber) {
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

		return verificationCode;
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
	
	
	// 기존 유저 find username, reset password
	@Override
    public boolean sendVerificationCodeToPhoneForExistingUser(String username, String phoneNumber) throws Exception {
		
		// 0. 기존 휴대폰번호와 일치하는지 확인
        String encryptedPhoneNumber = encryptionUtil.encrypt(phoneNumber);
		
        User foundUser = userRepo.findByEncryptedPhoneNumber(encryptedPhoneNumber);

        if (foundUser == null) {
        	return false;
        }

        if(!username.isEmpty() && !foundUser.getUsername().equals(username)) {
       		return false;
        }

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
    public boolean verifyEncryptedPhoneNumber(String phoneNumber, String verificationCode) {
		return phoneNumberVerificationService.verifyCode(phoneNumber, verificationCode);
    }
	
	@Override
    public String findUsernameByPhoneNumber(String phoneNumber) throws Exception {
		
        String encryptedPhoneNumber = encryptionUtil.encrypt(phoneNumber);
		User foundUser = userRepo.findByEncryptedPhoneNumber(encryptedPhoneNumber);
		if(foundUser == null) {
			return "User not found";
		} else {
			return foundUser.getUsername();			
		}
	}
	
	// 카카오 회원 찾기
	@Override
	public User KakaoFindId(String username) {
		User user = userRepo.findByUsername(username);
		return user;
	}

	// 카카오 액세스 토큰 요청
	/**
	 * @date : 2024. 9. 30.
	 * @user : 한진규 KakaogetAccessToken : 카카오 액세스 토큰 API 요청
	 *
	 * @param code
	 * @return TODO
	 */
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
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

		// Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
		ResponseEntity<String> response = 
				rt.exchange("https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class);
		
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
		System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());
		// return "Access_Token : Bearer+" +oauthToken.getAccess_token()+" Reflash_Token
		// : "+oauthToken.getRefresh_token();
		return "Bearer " + oauthToken.getAccess_token();
	}

	// 카카오 유저정보 가져오기 및 회원 DB 저장
	/**
	 * @date : 2024. 9. 30.
	 * @user : 한진규 KakaoSignUp : KakakoAuthLogin에서 토큰이 있을경우 유정 정보 API 요청
	 *
	 * @param kakaoToken
	 * @return TODO
	 */
	@Override
	public Map<String, Object> KakaoSignUp(OAuthToken kakaoToken) throws Exception {
		RestTemplate rt2 = new RestTemplate();
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + kakaoToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // 내가 지금 전송할 body data 가

		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

		ResponseEntity<String> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,
				kakaoProfileRequest, String.class);

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

		User user = User.builder()
						.username("kakao_" + kakaoProfile.getKakao_account().getEmail().split("@")[0])
						.password(encoder.encode(OAUTHPASSWORD))
						.encryptedPhoneNumber(kakaoProfile.getKakao_account().getEmail())
						.email(kakaoProfile.getKakao_account().getEmail())
						.role(UserRole.USER)
						.accountStatus(AccountStatus.ACTIVE)
						.verificationStatus(VerificationStatus.VERIFIED)
						.build();

		boolean isLoginChecked = false;

		User existingUser = userRepo.findByUsername(user.getUsername());
		if (existingUser != null) {
			System.out.println("이미 존재하는 유저입니다.");
			isLoginChecked =true;
		} else {
			// 5. 유저가 없을 경우 저장
			user = userRepo.save(user);
			System.out.println("새로운 유저가 저장되었습니다.");

			String oauthIdTypeChange=kakaoProfile.getId().toString();
			Oauth kakaoUser = Oauth.builder()
									.user(user)
									.oauth_provider("KAKAO")
									.oauth_id(oauthIdTypeChange)
									.build();
			oauthRepo.save(kakaoUser);

			String userProfilePicture = s3Service.uploadOAuthProfileImage(kakaoProfile.properties.profile_image);
			
			userProfileInfoRepo.save(UserProfileInfo.toEntity(
					UserProfileInfoDto.builder().nickname(kakaoProfile.getProperties().getNickname()).profilePicture(userProfilePicture).build(), user));

			userTripInfoRepo.save(UserTripInfo.builder().userId(user.getId()).build());


		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), OAUTHPASSWORD));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		JSONObject kakaoInfo2 = new JSONObject();
		kakaoInfo2.put("id", kakaoProfile.id);
		kakaoInfo2.put("nickname", kakaoProfile.getKakao_account().getProfile().getNickname());
		kakaoInfo2.put("profile_image", kakaoProfile.getKakao_account().getProfile().getProfile_image_url());
		kakaoInfo2.put("email", kakaoProfile.getKakao_account().getEmail());
		kakaoInfo2.put("kakaoToken", kakaoToken.getAccess_token());
		kakaoInfo2.put("kakaoRefreshToken", kakaoToken.getRefresh_token());
		kakaoInfo2.put("phonenum", user.getEncryptedPhoneNumber());

		System.out.println("kakaoInfo2: " + kakaoInfo2.toString()); // 보기 좋게 출력
		String token = jwtUtil.generateToken(user.getUsername());
		return Map.of("token", "Bearer+" + token,
				"isLoginChecked", isLoginChecked);
	}

	// 카카오 로그아웃
	/**
	 * @date : 2024. 9. 30.
	 * @user : 한진규 kakaoLogout : 카카오 로그아웃 요청 API
	 *
	 * @param oAuthToken
	 * @param kakaoId
	 * @return TODO
	 */
	@Override
	public String kakaoLogout(OAuthToken oAuthToken, Long kakaoId) {
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("target_id_type", "user_id");
		params.add("target_id", kakaoId.toString());

		HttpEntity<MultiValueMap<String, String>> kakaoLogoutRequest = new HttpEntity<>(headers);
		// 로그아웃 요청 (POST)
		ResponseEntity<String> response = 
				rt.exchange("https://kapi.kakao.com/v1/user/logout",
				HttpMethod.POST,
				kakaoLogoutRequest,
				String.class);
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

	// 구글 액세스 토큰 요청
	/**
	 * @date : 2024. 9. 30.
	 * @user : 한진규 GoogleAccessToken : 구글 로그인시 액새스 토큰 API 요청
	 *
	 * @param code
	 * @return TODO
	 */
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

		HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(params, headers);
		System.out.println(params);

		ResponseEntity<String> response = 
				rt.exchange("https://oauth2.googleapis.com/token",
				HttpMethod.POST,
				googleTokenRequest,
				String.class);

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
		System.out.println("구글 엑세스 토큰 : " + oauthToken.getAccess_token());
		return "Bearer " + oauthToken.getAccess_token();
	}

	// 구글 유저정보 가져오기 및 회원 DB 저장
	/**
	 * @date : 2024. 9. 30.
	 * @user : 한진규 GoogleSignUp : GoogleAuthLogin에서 토큰이 있을경우 유저정보 API 요청
	 *
	 * @param googleToken
	 * @return TODO
	 */
	@Override
	public Map<String, Object> GoogleSignUp(OAuthToken googleToken) throws Exception{
		RestTemplate rt2 = new RestTemplate();
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + googleToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<MultiValueMap<String, String>> googleProfileRequest = new HttpEntity<>(headers2);

		ResponseEntity<String> response2 = rt2
				.exchange("https://www.googleapis.com/userinfo/v2/me",
				HttpMethod.GET,
				googleProfileRequest,
				String.class);

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

		User user = User.builder()
						.username("google_" + googleProfile.getEmail().split("@")[0])
						.password(encoder.encode(OAUTHPASSWORD))
						.encryptedPhoneNumber(googleProfile.getEmail())
						.email(googleProfile.getEmail())
						.role(UserRole.USER)
						.accountStatus(AccountStatus.ACTIVE)
						.verificationStatus(VerificationStatus.VERIFIED)
						.build();
		
		boolean isLoginChecked = false;
		
		User existingUser = userRepo.findByUsername(user.getUsername());
		if (existingUser != null) {
			System.out.println("이미 존재하는 유저입니다.");
			isLoginChecked =true;
		} else {
			user = userRepo.save(user);
			System.out.println("새로운 유저가 저장되었습니다.");
			String userProfilePicture = s3Service.uploadOAuthProfileImage(googleProfile.getPicture());
			Oauth googleUser = Oauth.builder().user(user).oauth_provider("GOOGLE").oauth_id(googleProfile.getId())
					.build();
			oauthRepo.save(googleUser);

			userProfileInfoRepo.save(UserProfileInfo
					.toEntity(UserProfileInfoDto.builder().nickname(googleProfile.getName()).profilePicture(userProfilePicture).build(), user));

			userTripInfoRepo.save(UserTripInfo.builder().userId(user.getId()).build());
		

		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), OAUTHPASSWORD));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		JSONObject googleInfo2 = new JSONObject();
		googleInfo2.put("id", googleProfile.getId());
		googleInfo2.put("name", googleProfile.getName());
		googleInfo2.put("nickname", googleProfile.getGivenName());
		googleInfo2.put("picture", googleProfile.getPicture());
		googleInfo2.put("email", googleProfile.getEmail());
		googleInfo2.put("googleToken", googleToken.getAccess_token());
		googleInfo2.put("googleRefreshToken", googleToken.getRefresh_token());
		googleInfo2.put("phonenum", user.getEncryptedPhoneNumber());

		System.out.println("googleInfo2: " + googleInfo2.toString()); // 보기 좋게 출력
		String token = jwtUtil.generateToken(user.getUsername());
		return Map.of("token", "Bearer+" + token,
				"isLoginChecked", isLoginChecked);
	}

}