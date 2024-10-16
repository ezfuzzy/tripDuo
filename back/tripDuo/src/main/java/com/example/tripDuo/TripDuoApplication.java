package com.example.tripDuo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.tripDuo.entity.ChatMessage;
import com.example.tripDuo.entity.ChatParticipant;
import com.example.tripDuo.entity.ChatRoom;
import com.example.tripDuo.entity.Post;
import com.example.tripDuo.entity.ReportToChatMessage;
import com.example.tripDuo.entity.ReportToChatRoom;
import com.example.tripDuo.entity.ReportToPost;
import com.example.tripDuo.entity.ReportToUser;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.entity.UserFollow;
import com.example.tripDuo.entity.UserProfileInfo;
import com.example.tripDuo.enums.AccountStatus;
import com.example.tripDuo.enums.ChatRoomType;
import com.example.tripDuo.enums.FollowType;
import com.example.tripDuo.enums.Gender;
import com.example.tripDuo.enums.PostStatus;
import com.example.tripDuo.enums.PostType;
import com.example.tripDuo.enums.UserRole;
import com.example.tripDuo.enums.VerificationStatus;
import com.example.tripDuo.repository.ChatMessageRepository;
import com.example.tripDuo.repository.ChatParticipantRepository;
import com.example.tripDuo.repository.ChatRoomRepository;
import com.example.tripDuo.repository.PostRepository;
import com.example.tripDuo.repository.ReportRepository;
import com.example.tripDuo.repository.UserFollowRepository;
import com.example.tripDuo.repository.UserProfileInfoRepository;
import com.example.tripDuo.repository.UserRepository;
import com.example.tripDuo.util.EncryptionUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@PropertySource(value = "classpath:custom.properties")
@SpringBootApplication
@EnableScheduling
public class TripDuoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripDuoApplication.class, args);
	}
	
	@Autowired
	private ChatParticipantRepository chatParticipantsRepo;
	
	@Autowired
    private ChatRoomRepository chatRoomRepo;

    @Autowired
    private ChatMessageRepository messageRepo;
    
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserProfileInfoRepository userProfileInfoRepo;
	
	@Autowired
	private PostRepository postRepo;

	@Autowired
	private UserFollowRepository followRepo;

	@Autowired
	private ReportRepository reportRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private EncryptionUtil encryptionUtil;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@EventListener(ApplicationReadyEvent.class)
	public void init() throws Exception {
		System.out.println("\n### init ###");
		
		String[] socialLinks = {"tictok+", "instagram+"};
		
		User u1 = User.builder().username("ezfz").password(encoder.encode("hysz")).encryptedPhoneNumber(encryptionUtil.encrypt("01035837121")).email("ezfz@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.ADMIN).build(); 
		User savedUser1 = userRepo.save(u1);
		UserProfileInfo upi1 = UserProfileInfo.builder().user(savedUser1).nickname("ezfz").age((long) 28).profilePicture("9a926641-7e7f-4d23-8a78-1fe301813ccd.jpg").socialLinks(socialLinks).gender(Gender.MALE).build();
		upi1 = userProfileInfoRepo.save(upi1);
		
		User u2 = User.builder().username("aaaa").password(encoder.encode("aaaa")).encryptedPhoneNumber(encryptionUtil.encrypt("01011112222")).email("aaaa@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser2 = userRepo.save(u2);
		UserProfileInfo upi2 = UserProfileInfo.builder().user(savedUser2).nickname("user01").profilePicture("e68b541e-8fb9-4d13-8358-6d2111303fa6.png").gender(Gender.MALE).build();
		upi2 = userProfileInfoRepo.save(upi2);

		User u3 = User.builder().username("bbbb").password(encoder.encode("bbbb")).encryptedPhoneNumber(encryptionUtil.encrypt("01022223333")).email("bbbb@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser3 = userRepo.save(u3);
		UserProfileInfo upi3 = UserProfileInfo.builder().user(savedUser3).nickname("user02").gender(Gender.FEMALE).build();
		upi3 = userProfileInfoRepo.save(upi3);

		User u4 = User.builder().username("cccc").password(encoder.encode("cccc")).encryptedPhoneNumber(encryptionUtil.encrypt("01033334444")).email("cccc@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser4 = userRepo.save(u4);
		UserProfileInfo upi4 = UserProfileInfo.builder().user(savedUser4).nickname("user03").gender(Gender.FEMALE).build();
		upi4 = userProfileInfoRepo.save(upi4);

		User u5 = User.builder().username("dddd").password(encoder.encode("dddd")).encryptedPhoneNumber(encryptionUtil.encrypt("01055556666")).email("dddd@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser5 = userRepo.save(u5);
		UserProfileInfo upi5 = UserProfileInfo.builder().user(savedUser5).nickname("user04").gender(Gender.MALE).build();
		upi5 = userProfileInfoRepo.save(upi5);

		User u6 = User.builder().username("user1").password(encoder.encode("password1")).encryptedPhoneNumber(encryptionUtil.encrypt("01012345678")).email("user1@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser6 = userRepo.save(u6);
		UserProfileInfo upi6 = UserProfileInfo.builder().user(savedUser6).nickname("user05").age((long) 25).ratings((long) 1300).gender(Gender.MALE).profilePicture("e68b541e-8fb9-4d13-8358-6d2111303fa6.png").profileMessage("안녕하세요1").curLocation("서울").build();
		upi6 = userProfileInfoRepo.save(upi6);

		User u7 = User.builder().username("user2").password(encoder.encode("password2")).encryptedPhoneNumber(encryptionUtil.encrypt("01023456789")).email("user2@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser7 = userRepo.save(u7);
		UserProfileInfo upi7 = UserProfileInfo.builder().user(savedUser7).nickname("user06").age((long) 30).ratings((long) 1600).gender(Gender.MALE).profileMessage("안녕하세요2").curLocation("부산").build();
		upi7 = userProfileInfoRepo.save(upi7);

		User u8 = User.builder().username("user3").password(encoder.encode("password3")).encryptedPhoneNumber(encryptionUtil.encrypt("01034567890")).email("user3@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser8 = userRepo.save(u8);
		UserProfileInfo upi8 = UserProfileInfo.builder().user(savedUser8).nickname("user07").age((long) 22).ratings((long) 3200).gender(Gender.FEMALE).profileMessage("안녕하세요3").curLocation("대구").build();
		upi8 = userProfileInfoRepo.save(upi8);

		User u9 = User.builder().username("user4").password(encoder.encode("password4")).encryptedPhoneNumber(encryptionUtil.encrypt("01045678901")).email("user4@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser9 = userRepo.save(u9);
		UserProfileInfo upi9 = UserProfileInfo.builder().user(savedUser9).nickname("user08").age((long) 28).ratings((long) 4600).gender(Gender.MALE).profileMessage("안녕하세요4").curLocation("인천").build();
		upi9 = userProfileInfoRepo.save(upi9);

		User u10 = User.builder().username("user5").password(encoder.encode("password5")).encryptedPhoneNumber(encryptionUtil.encrypt("01056789012")).email("user5@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser10 = userRepo.save(u10);
		UserProfileInfo upi10 = UserProfileInfo.builder().user(savedUser10).nickname("user09").age((long) 35).ratings((long) 6700).gender(Gender.MALE).profileMessage("안녕하세요5").curLocation("광주").build();
		upi10 = userProfileInfoRepo.save(upi10);
		
		User u11 = User.builder().username("user06").password(encoder.encode("password6")).encryptedPhoneNumber(encryptionUtil.encrypt("01067890123")).email("user6@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser11 = userRepo.save(u11);
		UserProfileInfo upi11 = UserProfileInfo.builder().user(savedUser11).nickname("user10").age((long) 35).ratings((long) 6700).gender(Gender.MALE).profileMessage("안녕하세요5").curLocation("광주").build();
		upi11 = userProfileInfoRepo.save(upi11);
		
		User u12 = User.builder().username("user07").password(encoder.encode("password7")).encryptedPhoneNumber(encryptionUtil.encrypt("01078901234")).email("user7@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser12 = userRepo.save(u12);
		UserProfileInfo upi12 = UserProfileInfo.builder().user(savedUser12).nickname("user11").age((long) 35).ratings((long) 6700).gender(Gender.MALE).profileMessage("안녕하세요5").curLocation("광주").build();
		upi12 = userProfileInfoRepo.save(upi12);
		
		User u13 = User.builder().username("user08").password(encoder.encode("password8")).encryptedPhoneNumber(encryptionUtil.encrypt("01089012345")).email("user8@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser13 = userRepo.save(u13);
		UserProfileInfo upi13 = UserProfileInfo.builder().user(savedUser13).nickname("user12").age((long) 35).ratings((long) 6700).gender(Gender.MALE).profileMessage("안녕하세요5").curLocation("광주").build();
		upi13 = userProfileInfoRepo.save(upi13);
		
		User u14 = User.builder().username("user09").password(encoder.encode("password9")).encryptedPhoneNumber(encryptionUtil.encrypt("01090123456")).email("user9@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser14 = userRepo.save(u14);
		UserProfileInfo upi14 = UserProfileInfo.builder().user(savedUser14).nickname("user13").age((long) 35).ratings((long) 6700).gender(Gender.MALE).profileMessage("안녕하세요5").curLocation("광주").build();
		upi14 = userProfileInfoRepo.save(upi14);
		
		User u15 = User.builder().username("user10").password(encoder.encode("password10")).encryptedPhoneNumber(encryptionUtil.encrypt("01098765432")).email("user10@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser15 = userRepo.save(u15);
		UserProfileInfo upi15 = UserProfileInfo.builder().user(savedUser15).nickname("user14").age((long) 35).ratings((long) 6700).gender(Gender.MALE).profileMessage("안녕하세요5").curLocation("광주").build();
		upi15 = userProfileInfoRepo.save(upi15);

		
		Post p1 = Post.builder().userProfileInfo(upi6).type(PostType.MATE).title("메이트 구해용").content("내용입니다 111").country("대한민국").city("제주").viewCount(123L).likeCount(18L).status(PostStatus.OPEN).build();
		Post p2 = Post.builder().userProfileInfo(upi7).type(PostType.MATE).title("착한 메이트 구해용").content("내용입니다 222").country("미국").city("로스앤젤레스").viewCount(234L).likeCount(28L).status(PostStatus.OPEN).build();
		Post p3 = Post.builder().userProfileInfo(upi8).type(PostType.MATE).title("러시아 동행하실분").content("내용입니다 333").country("러시아").city("모스크바").viewCount(456L).likeCount(38L).status(PostStatus.CLOSED).build();
		Post p4 = Post.builder().userProfileInfo(upi9).type(PostType.MATE).title("영국에서 피쉬앤 칩스 드실분").content("내용입니다 444").country("영국").city("런던").viewCount(345L).likeCount(78L).status(PostStatus.CLOSED).build();
		Post p5 = Post.builder().userProfileInfo(upi10).type(PostType.MATE).title("같이 와인밭 가보실분ㅜㅜ 패키지라 2인 이상 필수에요").content("내용입니다 555").country("프랑스").city("파리").viewCount(567L).likeCount(128L).status(PostStatus.OPEN).build();
		Post p6 = Post.builder().userProfileInfo(upi6).type(PostType.MATE).title("스페인 혼자가는데 무서워서 같이 가실 메이트 구해요").content("내용입니다 666").country("스페인").city("바르셀로나").viewCount(789L).likeCount(98L).status(PostStatus.CLOSED).build();
		
		Map<String, Object> place7_1 = new HashMap<>();
		place7_1.put("id", "10808261");
		place7_1.put("phone", "1661-2626");
		place7_1.put("dayIndex", 0);
		place7_1.put("position", Map.of("La", 126.49272304493574, "Ma", 33.50683984835887));
		place7_1.put("placeMemo", "9시 도착");
		place7_1.put("place_url", "http://place.map.kakao.com/10808261");
		place7_1.put("placeIndex", 0);
		place7_1.put("place_name", "제주국제공항");
		place7_1.put("address_name", "제주특별자치도 제주시 용담이동 2002");
		place7_1.put("category_name", "교통,수송 > 교통시설 > 공항");
		place7_1.put("road_address_name", "제주특별자치도 제주시 공항로 2");

		Map<String, Object> place7_2 = new HashMap<>();
		place7_2.put("id", "55095867");
		place7_2.put("phone", "");
		place7_2.put("dayIndex", 0);
		place7_2.put("position", Map.of("La", 126.396035021153, "Ma", 33.4192918926001));
		place7_2.put("placeMemo", "10시 하우스오브레퓨즈 전시구경");
		place7_2.put("place_url", "http://place.map.kakao.com/55095867");
		place7_2.put("placeIndex", 1);
		place7_2.put("place_name", "하우스오브레퓨즈");
		place7_2.put("address_name", "제주특별자치도 제주시 애월읍 유수암리 2819");
		place7_2.put("category_name", "문화,예술 > 문화시설");
		place7_2.put("road_address_name", "제주특별자치도 제주시 애월읍 하소로 735");

		Map<String, Object> day1 = new HashMap<>();
		day1.put("places", List.of(place7_1, place7_2));
		day1.put("dayMemo", "먹고 자고 싸돌아다니고");

		List<Map<String, Object>> postDataList = List.of(day1);
		JsonNode postDataJson7 = objectMapper.convertValue(postDataList, JsonNode.class);

		Post p7 = Post.builder()
		        .userProfileInfo(upi10)  // 실제 UserProfileInfo 객체를 넣어야 합니다.
		        .type(PostType.COURSE)
		        .title("제주도 1박2일 빡빡한 일정")
		        .content(null)
		        .country("대한민국")
		        .city("제주")
		        .startDate("2024-10-24T15:00:00.000Z")
		        .endDate("2024-10-26T14:59:59.999Z")
		        .postData(postDataJson7)  // JSON으로 변환된 배열 형태의 postData
		        .tags(new String[] {"#체력", "#시간은돈", "#금요일연차"})
		        .viewCount(666L)
		        .likeCount(666L)
		        .commentCount(0L)
		        .rating(0.0F)
		        .status(PostStatus.PUBLIC)
		        .createdAt(LocalDateTime.of(2024, 10, 12, 16, 37, 4, 177342))
		        .build();
		
		Map<String, Object> place8_1 = new HashMap<>();
		place8_1.put("id", "1756449505");
		place8_1.put("phone", "02-777-7376");
		place8_1.put("dayIndex", 0);
		place8_1.put("position", Map.of("La", 126.985832385269, "Ma", 37.5644547619695));
		place8_1.put("placeMemo", "시작은 남산에서 가까운 번화가인 명동에서! 명동성당 근처 맛집이니 방문해보자.");
		place8_1.put("place_url", "http://place.map.kakao.com/1756449505");
		place8_1.put("placeIndex", 0);
		place8_1.put("place_name", "명동고로케");
		place8_1.put("address_name", "서울 중구 명동1가 6-1");
		place8_1.put("category_name", "음식점 > 간식");
		place8_1.put("road_address_name", "서울 중구 명동11길 9");

		Map<String, Object> place8_2 = new HashMap<>();
		place8_2.put("id", "8246127");
		place8_2.put("phone", "02-6358-5533");
		place8_2.put("dayIndex", 0);
		place8_2.put("position", Map.of("La", 126.99443158318867, "Ma", 37.559311690784995));
		place8_2.put("placeMemo", "고즈넉한 분위기를 즐기며 찻집에서 여유로운 시간을..");
		place8_2.put("place_url", "http://place.map.kakao.com/8246127");
		place8_2.put("placeIndex", 1);
		place8_2.put("place_name", "남산골한옥마을");
		place8_2.put("address_name", "서울 중구 필동2가 84-1");
		place8_2.put("category_name", "여행 > 관광,명소");
		place8_2.put("road_address_name", "서울 중구 퇴계로34길 28");

		Map<String, Object> place8_3 = new HashMap<>();
		place8_3.put("id", "1642591897");
		place8_3.put("phone", "02-777-7929");
		place8_3.put("dayIndex", 0);
		place8_3.put("position", Map.of("La", 126.985746661115, "Ma", 37.5569494480227));
		place8_3.put("placeMemo", "남산의 명물 돈까스. 어느 돈까스 집을 가야할지 모르겠다면 여기로!");
		place8_3.put("place_url", "http://place.map.kakao.com/1642591897");
		place8_3.put("placeIndex", 2);
		place8_3.put("place_name", "101번지 남산돈까스 본점");
		place8_3.put("address_name", "서울 중구 남산동2가 49-24");
		place8_3.put("category_name", "음식점 > 일식 > 돈까스,우동");
		place8_3.put("road_address_name", "서울 중구 소파로 101");

		Map<String, Object> place8_4 = new HashMap<>();
		place8_4.put("id", "8233285");
		place8_4.put("phone", "02-753-2403");
		place8_4.put("dayIndex", 0);
		place8_4.put("position", Map.of("La", 126.98403099725546, "Ma", 37.55658072049463));
		place8_4.put("placeMemo", "대표적인 이동수단. 왕복표를 사는게 이득이다.");
		place8_4.put("place_url", "http://place.map.kakao.com/8233285");
		place8_4.put("placeIndex", 3);
		place8_4.put("place_name", "남산케이블카");
		place8_4.put("address_name", "서울 중구 회현동1가 산 1-19");
		place8_4.put("category_name", "여행 > 관광,명소 > 케이블카");
		place8_4.put("road_address_name", "서울 중구 소파로 83");

		Map<String, Object> place8_5 = new HashMap<>();
		place8_5.put("id", "1629378951");
		place8_5.put("phone", "02-3455-9277");
		place8_5.put("dayIndex", 0);
		place8_5.put("position", Map.of("La", 126.988230622132, "Ma", 37.5513049702718));
		place8_5.put("placeMemo", "전망대 이용시간 월~금,일 : 오전10시~ 오후11시, 토: 오전10시~오후12시");
		place8_5.put("place_url", "http://place.map.kakao.com/1629378951");
		place8_5.put("placeIndex", 4);
		place8_5.put("place_name", "N서울타워");
		place8_5.put("address_name", "서울 용산구 용산동2가 산 1-3");
		place8_5.put("category_name", "여행 > 관광,명소 > 전망대");
		place8_5.put("road_address_name", "서울 용산구 남산공원길 105");

		Map<String, Object> day8_1 = new HashMap<>();
		day8_1.put("places", List.of(place8_1, place8_2, place8_3, place8_4, place8_5));
		day8_1.put("dayMemo", "서울 남산 탐방 코스");

		List<Map<String, Object>> postDataList8 = List.of(day8_1);
		JsonNode postDataJson8 = objectMapper.convertValue(postDataList8, JsonNode.class);

		Post p8 = Post.builder()
		        .userProfileInfo(upi11)  // 실제 UserProfileInfo 객체를 넣어야 합니다.
		        .type(PostType.COURSE)
		        .title("서울 남산 1일 코스")
		        .content(null)
		        .country("대한민국")
		        .city("서울")
		        .startDate("2024-11-10T09:00:00.000Z")
		        .endDate("2024-11-10T18:00:00.000Z")
		        .postData(postDataJson8)
		        .tags(new String[] {"#서울여행", "#남산", "#맛집탐방", "#전망대"})
		        .viewCount(777L)
		        .likeCount(777L)
		        .commentCount(0L)
		        .rating(0.0F)
		        .status(PostStatus.PUBLIC)
		        .createdAt(LocalDateTime.now())
		        .build();
		Post p9 = Post.builder().userProfileInfo(upi8).type(PostType.COURSE).title("course 3").content("내용입니다 666").country("프랑스").city("파리").viewCount(789L).likeCount(98L).rating(3.3F).status(PostStatus.DELETED).build();
		Post p10 = Post.builder().userProfileInfo(upi9).type(PostType.COURSE).title("course 4").content("내용입니다 666").country("미국").city("로스앤젤레스").viewCount(789L).likeCount(98L).rating(4.7F).status(PostStatus.CLOSED).build();
		Post p11 = Post.builder().userProfileInfo(upi10).type(PostType.COURSE).title("course 5").content("내용입니다 666").country("호주").city("멜버른").viewCount(789L).likeCount(98L).rating(4.8F).status(PostStatus.CLOSED).build();
		Post p12 = Post.builder().userProfileInfo(upi10).type(PostType.COURSE).title("course 6").content("내용입니다 12").country("대한민국").city("인천").viewCount(789L).likeCount(98L).rating(3.6F).status(PostStatus.CLOSED).build();
		Post p13 = Post.builder().userProfileInfo(upi10).type(PostType.COURSE).title("course 7").content("내용입니다 13").country("대한민국").city("제주").viewCount(789L).likeCount(98L).rating(4.9F).status(PostStatus.CLOSED).build();
		Post p14 = Post.builder().userProfileInfo(upi10).type(PostType.COURSE).title("course 8").content("내용입니다 14").country("대한민국").city("서울").viewCount(789L).likeCount(98L).rating(3.9F).status(PostStatus.CLOSED).build();
		Post p15 = Post.builder().userProfileInfo(upi6).type(PostType.COURSE).title("도쿄에서 힐링여행").content("도쿄 힐링여행 코스입니당.").country("일본").city("도쿄").viewCount(450L).likeCount(55L).status(PostStatus.OPEN).build();
		Post p16 = Post.builder().userProfileInfo(upi2).type(PostType.COURSE).title("오사카에서 랜드마크 여행").content("오사카 랜드마크 여행 코스입니다.").country("일본").city("오사카").viewCount(320L).likeCount(40L).status(PostStatus.OPEN).build();
		Post p17 = Post.builder().userProfileInfo(upi3).type(PostType.COURSE).title("교토도 요즘 좋아요").content("교토여행 코스 작성해봤어요 ~").country("일본").city("교토").viewCount(570L).likeCount(65L).status(PostStatus.OPEN).build();
		Post p18 = Post.builder().userProfileInfo(upi4).type(PostType.COURSE).title("삿포로 겨울 여행").content("겨울에 아름다운 삿포로").country("일본").city("삿포로").viewCount(420L).likeCount(50L).status(PostStatus.OPEN).build();

		Post p19 = Post.builder().userProfileInfo(upi5).type(PostType.COURSE).title("천년의 역사를 걷다: 베이징 4일 여행").content("첫날은 자금성과 천안문 광장을 방문하여 중국의 역사와 문화를 체험합니다. 둘째 날은 만리장성에서 자연과 웅장한 역사를 동시에 느끼며, 셋째 날은 이화원을 방문하여 황실의 아름다움을 감상합니다. 마지막 날은 후퉁과 같은 전통 골목길을 탐방하며 현지 문화를 체험합니다.").country("중국").city("베이징").viewCount(300L).likeCount(35L).status(PostStatus.OPEN).build();
		Post p20 = Post.builder().userProfileInfo(upi6).type(PostType.COURSE).title("현대와 전통이 만나는 곳: 상하이 3일 여행").content("첫날은 와이탄에서 상하이의 현대적인 스카이라인을 감상하고, 난징동루에서 쇼핑을 즐깁니다. 둘째 날은 상하이 박물관과 예원에서 중국의 전통을 탐방하며, 마지막 날은 디즈니랜드에서 가족과 함께 즐거운 시간을 보냅니다.").country("중국").city("상하이").viewCount(510L).likeCount(75L).status(PostStatus.OPEN).build();
		Post p21 = Post.builder().userProfileInfo(upi7).type(PostType.COURSE).title("음식과 전통의 도시: 광저우 미식 여행").content("광저우는 미식가들의 천국입니다. 첫날은 진한 전통 딤섬을 맛보며 여행을 시작하고, 광저우 타워를 방문하여 도시 전경을 감상합니다. 둘째 날은 화청골과 안탑을 탐방하며 도시의 역사를 느끼고, 마지막 날은 샤먼대교를 따라 아름다운 자연을 즐깁니다.").country("중국").city("광저우").viewCount(280L).likeCount(45L).status(PostStatus.OPEN).build();
		Post p22 = Post.builder().userProfileInfo(upi8).type(PostType.COURSE).title("중국 고대 문명의 중심지: 시안 문화 여행").content("시안은 중국의 고대 역사와 문명이 살아 숨 쉬는 도시입니다. 첫날은 병마용 유적을 방문하여 진시황제의 역사를 배우고, 둘째 날은 화산을 등반하며 아름다운 자연 경관을 즐깁니다. 마지막 날은 시안 성벽을 자전거로 돌며 도시의 전경을 감상합니다.").country("중국").city("시안").viewCount(410L).likeCount(60L).status(PostStatus.OPEN).build();

		Post p23 = Post.builder().userProfileInfo(upi9).type(PostType.COURSE).title("인도 전통과 현대가 만나는 델리 탐방").content("첫날은 인도의 정치와 역사를 대표하는 인디아 게이트와 레드 포트를 방문합니다. 둘째 날은 국립 박물관에서 인도의 고대 유물을 감상하고, 셋째 날은 후마윤의 묘와 꾸뜹 미나르를 탐방하여 델리의 역사와 문화를 체험합니다.").country("인도").city("델리").viewCount(530L).likeCount(80L).status(PostStatus.OPEN).build();
		Post p24 = Post.builder().userProfileInfo(upi10).type(PostType.COURSE).title("볼리우드와 해안 도시의 매력: 뭄바이 3일 여행").content("첫날은 게이트웨이 오브 인디아와 차트라파티 시바지 터미널을 방문합니다. 둘째 날은 마린 드라이브에서 해안의 멋진 전경을 즐기며, 볼리우드 영화 스튜디오를 탐방합니다. 마지막 날은 엘리펀타 섬에서 고대 동굴 사원을 탐험하며 여행을 마무리합니다.").country("인도").city("뭄바이").viewCount(460L).likeCount(70L).status(PostStatus.OPEN).build();
		Post p25 = Post.builder().userProfileInfo(upi6).type(PostType.COURSE).title("문학과 예술의 중심지: 콜카타 문화 탐방").content("첫날은 빅토리아 기념관과 하우라 다리를 방문하여 도시의 상징을 감상하고, 둘째 날은 콜카타의 전통 요리와 예술 시장을 체험합니다. 마지막 날은 칼리 사원과 타고르의 생가를 탐방하며 인도의 문학적 유산을 배웁니다.").country("인도").city("콜카타").viewCount(400L).likeCount(65L).status(PostStatus.OPEN).build();
		Post p26 = Post.builder().userProfileInfo(upi7).type(PostType.COURSE).title("IT 도시에서 자연과 함께: 벵갈루루 3일 여행").content("첫날은 벵갈루루의 현대적 IT 허브를 탐방하며 도시의 혁신적인 발전을 체험합니다. 둘째 날은 큐 뱅크 식물원과 나나디 언덕에서 자연을 만끽하고, 마지막 날은 벵갈루루 궁전과 비다나 사우다를 방문하여 도시의 문화와 건축을 감상합니다.").country("인도").city("벵갈루루").viewCount(320L).likeCount(40L).status(PostStatus.OPEN).build();
		
		Post p27 = Post.builder().userProfileInfo(upi8).type(PostType.COURSE).title("가우디의 도시: 바르셀로나 건축 여행").content("첫날은 사그라다 파밀리아와 구엘 공원을 방문하여 가우디의 걸작을 감상합니다. 둘째 날은 고딕 지구를 탐방하며 역사적 건축물을 둘러보고, 셋째 날은 캄프 누에서 FC 바르셀로나의 축구 열기를 체험합니다.").country("스페인").city("바르셀로나").viewCount(510L).likeCount(90L).status(PostStatus.OPEN).build();
		Post p28 = Post.builder().userProfileInfo(upi9).type(PostType.COURSE).title("알함브라의 아름다움: 그라나다 2일 여행").content("첫날은 알함브라 궁전을 방문하여 이슬람 건축의 정수를 경험하고, 둘째 날은 알바이신 지구를 탐방하며 전통적인 안달루시아 문화를 느낍니다.").country("스페인").city("그라나다").viewCount(470L).likeCount(80L).status(PostStatus.OPEN).build();
		Post p29 = Post.builder().userProfileInfo(upi6).type(PostType.COURSE).title("스페인의 심장: 마드리드 3일 여행").content("첫날은 프라도 미술관에서 스페인의 예술 작품을 감상하고, 둘째 날은 마요르 광장과 푸에르타 델 솔을 둘러보며 현지 문화를 체험합니다. 마지막 날은 레티로 공원에서 여유로운 시간을 보내며 여행을 마무리합니다.").country("스페인").city("마드리드").viewCount(340L).likeCount(50L).status(PostStatus.OPEN).build();
		Post p30 = Post.builder().userProfileInfo(upi7).type(PostType.COURSE).title("플라멩코의 도시: 세비야 2일 여행").content("첫날은 세비야 대성당과 히랄다 탑을 방문하여 스페인의 역사적 건축을 감상하고, 둘째 날은 플라멩코 공연을 관람하며 전통 스페인 문화를 체험합니다.").country("스페인").city("세비야").viewCount(290L).likeCount(45L).status(PostStatus.OPEN).build();

		Post p31 = Post.builder().userProfileInfo(upi8).type(PostType.COURSE).title("역사와 현대가 공존하는 도시: 런던 4일 여행").content("첫날은 타워 브릿지와 런던 타워를 방문하여 영국의 역사적 명소를 탐방하고, 둘째 날은 버킹엄 궁전과 웨스트민스터 사원을 둘러봅니다. 셋째 날은 브리티시 뮤지엄에서 다양한 문화를 접하고, 마지막 날은 런던아이에서 도시 전경을 감상합니다.").country("영국").city("런던").viewCount(580L).likeCount(85L).status(PostStatus.OPEN).build();
		Post p32 = Post.builder().userProfileInfo(upi9).type(PostType.COURSE).title("음악과 스포츠의 도시: 맨체스터 탐방").content("맨체스터는 세계적인 음악과 축구의 중심지입니다. 첫날은 유명한 맨체스터 유나이티드 스타디움을 방문하고, 둘째 날은 역사적인 맨체스터 음악 씬을 탐방하며 다양한 공연을 즐깁니다. 마지막 날에는 맨체스터의 미술관과 박물관에서 도시의 문화를 경험합니다.").country("영국").city("맨체스터").viewCount(350L).likeCount(55L).status(PostStatus.OPEN).build();
		Post p33 = Post.builder().userProfileInfo(upi5).type(PostType.COURSE).title("산업 혁명의 발상지: 버밍엄 여행").content("버밍엄은 산업 혁명의 중심지로, 첫날은 버밍엄의 역사적인 산업 유적지를 탐방합니다. 둘째 날은 보행자 거리에서 쇼핑과 음식 문화를 체험하고, 마지막 날은 버밍엄 미술관에서 예술 작품을 감상합니다.").country("영국").city("버밍엄").viewCount(370L).likeCount(60L).status(PostStatus.OPEN).build();
		Post p34 = Post.builder().userProfileInfo(upi2).type(PostType.COURSE).title("비틀즈와 항구 도시: 리버풀 여행").content("리버풀은 비틀즈의 고향입니다. 첫날은 비틀즈 스토리 박물관을 방문하고, 둘째 날은 리버풀 항구를 따라 산책하며 도시의 아름다움을 느낍니다. 마지막 날은 리버풀의 음악 씬에서 다양한 라이브 공연을 즐깁니다.").country("영국").city("리버풀").viewCount(430L).likeCount(70L).status(PostStatus.OPEN).build();

		Post p35 = Post.builder().userProfileInfo(upi1).type(PostType.COURSE).title("역사와 현대의 조화: 베를린 탐방").content("베를린은 역사와 현대가 공존하는 도시입니다. 첫날은 브란덴부르크 문과 유대인 기념관을 방문하고, 둘째 날은 현대 미술관에서 전시를 관람합니다. 마지막 날은 베를린 장벽을 따라 역사적 유적지를 탐방합니다.").country("독일").city("베를린").viewCount(620L).likeCount(95L).status(PostStatus.OPEN).build();
		Post p36 = Post.builder().userProfileInfo(upi2).type(PostType.COURSE).title("맥주와 문화의 도시: 뮌헨 여행").content("뮌헨은 세계적인 맥주와 문화를 자랑합니다. 첫날은 유명한 마리엔플라츠를 방문하고, 둘째 날은 잉글리시 가든에서 휴식을 취한 후, 맥주 정원에서 현지 맥주를 즐깁니다. 마지막 날은 뮌헨의 역사적 건축물들을 탐방합니다.").country("독일").city("뮌헨").viewCount(520L).likeCount(85L).status(PostStatus.OPEN).build();
		Post p37 = Post.builder().userProfileInfo(upi3).type(PostType.COURSE).title("비즈니스 중심지: 프랑크푸르트 탐방").content("프랑크푸르트는 독일의 금융 중심지입니다. 첫날은 뢰머를 방문하고, 둘째 날은 주요 은행 건물과 상징적인 스카이라인을 감상합니다. 마지막 날은 프랑크푸르트의 다양한 박물관을 탐방합니다.").country("독일").city("프랑크푸르트").viewCount(490L).likeCount(80L).status(PostStatus.OPEN).build();
		Post p38 = Post.builder().userProfileInfo(upi4).type(PostType.COURSE).title("항구 도시의 매력: 함부르크 여행").content("함부르크는 매력적인 항구 도시입니다. 첫날은 함부르크 항구와 미하엘 성당을 방문하고, 둘째 날은 스피차 시를 따라 자전거 투어를 즐깁니다. 마지막 날에는 엘프필하모니에서 공연을 관람합니다.").country("독일").city("함부르크").viewCount(470L).likeCount(75L).status(PostStatus.OPEN).build();

		Post p39 = Post.builder().userProfileInfo(upi5).type(PostType.COURSE).title("사랑과 예술의 도시: 파리 여행").content("파리는 로맨틱한 분위기로 유명합니다. 첫날은 에펠탑과 루브르 박물관을 방문하고, 둘째 날은 세느강에서 유람선을 타며 도시를 감상합니다. 마지막 날은 몽마르트르 언덕에서 예술의 숨결을 느껴봅니다.").country("프랑스").city("파리").viewCount(640L).likeCount(100L).status(PostStatus.OPEN).build();
		Post p40 = Post.builder().userProfileInfo(upi6).type(PostType.COURSE).title("지중해의 보석: 마르세유 여행").content("마르세유는 지중해의 아름다움을 자랑합니다. 첫날은 구항구를 탐방하고, 둘째 날은 칼랑크 국립공원에서 하이킹을 즐깁니다. 마지막 날은 현지 해산물 요리를 맛보며 마르세유의 매력을 느껴봅니다.").country("프랑스").city("마르세유").viewCount(420L).likeCount(60L).status(PostStatus.OPEN).build();
		Post p41 = Post.builder().userProfileInfo(upi7).type(PostType.COURSE).title("프랑스의 미식과 문화: 리옹 탐방").content("리옹은 미식의 도시로 유명합니다. 첫날은 올드 리옹을 탐방하고, 둘째 날은 유명한 식당에서 미식을 경험합니다. 마지막 날은 리옹의 박물관과 미술관을 방문합니다.").country("프랑스").city("리옹").viewCount(460L).likeCount(70L).status(PostStatus.OPEN).build();
		Post p42 = Post.builder().userProfileInfo(upi8).type(PostType.COURSE).title("해변과 도시의 조화: 니스 여행").content("니스는 해변과 도시의 조화가 매력적입니다. 첫날은 프로방스의 아름다운 해변에서 휴식을 취하고, 둘째 날은 니스의 구시가지 탐방합니다. 마지막 날에는 주변의 몬테카를로를 방문합니다.").country("프랑스").city("니스").viewCount(430L).likeCount(65L).status(PostStatus.OPEN).build();

		Post p43 = Post.builder().userProfileInfo(upi9).type(PostType.COURSE).title("고대 로마의 매력: 로마 탐방").content("로마는 고대 문명의 중심지입니다. 첫날은 콜로세움을 방문하고, 둘째 날은 바티칸 시국의 성 베드로 대성당을 탐방합니다. 마지막 날은 로마의 다양한 역사적 유적지를 경험합니다.").country("이탈리아").city("로마").viewCount(540L).likeCount(85L).status(PostStatus.OPEN).build();
		Post p44 = Post.builder().userProfileInfo(upi3).type(PostType.COURSE).title("패션과 예술의 도시: 밀라노 여행").content("밀라노는 패션과 예술의 중심지입니다. 첫날은 두오모 성당을 방문하고, 둘째 날은 스칼라 극장에서 공연을 관람합니다. 마지막 날은 밀라노의 패션 거리에서 쇼핑을 즐깁니다.").country("이탈리아").city("밀라노").viewCount(370L).likeCount(55L).status(PostStatus.OPEN).build();
		Post p45 = Post.builder().userProfileInfo(upi1).type(PostType.COURSE).title("수운의 도시: 베네치아 탐방").content("베네치아는 수운의 도시로, 첫날은 곤돌라를 타고 운하를 탐방합니다. 둘째 날은 산 마르코 광장과 대성당을 방문하고, 마지막 날은 베네치아의 예술과 문화를 느낄 수 있는 미술관을 관람합니다.").country("이탈리아").city("베네치아").viewCount(500L).likeCount(80L).status(PostStatus.OPEN).build();
		Post p46 = Post.builder().userProfileInfo(upi2).type(PostType.COURSE).title("르네상스의 도시: 피렌체 탐방").content("피렌체는 르네상스의 발상지입니다. 첫날은 우피치 미술관을 방문하고, 둘째 날은 두오모 성당과 피렌체 대성당을 탐방합니다. 마지막 날은 피렌체의 매력적인 거리에서 미식을 즐깁니다.").country("이탈리아").city("피렌체").viewCount(460L).likeCount(70L).status(PostStatus.OPEN).build();

		Post p47 = Post.builder().userProfileInfo(upi3).type(PostType.COURSE).title("끝없는 매력의 대도시, 뉴욕 5일 여행 계획").content("뉴욕은 상징적인 랜드마크와 다채로운 문화로 가득 찬 도시입니다. 이 여행 계획에서는 자유의 여신상, 센트럴 파크, 타임스퀘어, 브루클린 브릿지 등 필수적인 명소들을 방문합니다. 또한 브로드웨이 뮤지컬 관람과 현대 미술관(MoMA) 방문으로 뉴욕의 예술과 문화를 체험하며, 소호와 첼시 시장에서 맛있는 음식과 쇼핑을 즐깁니다.").country("미국").city("뉴욕").viewCount(620L).likeCount(90L).status(PostStatus.OPEN).build();
		Post p48 = Post.builder().userProfileInfo(upi4).type(PostType.COURSE).title("할리우드의 꿈과 비치 라이프를 만끽하는 LA 여행 4일 코스").content("로스앤젤레스는 엔터테인먼트와 해변을 함께 즐길 수 있는 도시입니다. 이 여행에서는 할리우드 명예의 거리와 그리피스 천문대를 방문하여 LA의 아이코닉한 풍경을 감상하고, 산타모니카 해변과 베니스 비치에서 여유로운 시간을 보냅니다. 게티 센터에서 문화와 예술을 경험하고, 말리부 해안 드라이브를 통해 캘리포니아의 아름다움을 만끽합니다.").country("미국").city("로스앤젤레스").viewCount(560L).likeCount(85L).status(PostStatus.OPEN).build();
		Post p49 = Post.builder().userProfileInfo(upi5).type(PostType.COURSE).title("바람의 도시 시카고에서의 4일 간의 건축과 예술 여행").content("시카고는 현대 건축의 메카이자 예술의 중심지입니다. 첫날은 시카고 리버 크루즈를 통해 시카고의 고층 건물을 감상하고, 윌리스 타워에서 도시의 전경을 한눈에 봅니다. 둘째 날은 아트 인스티튜트 시카고를 방문하여 세계적인 예술 작품을 감상하고, 밀레니엄 파크에서 여유를 즐깁니다. 마지막 날은 네이비 피어에서의 산책과 맛있는 음식으로 마무리합니다.").country("미국").city("시카고").viewCount(490L).likeCount(70L).status(PostStatus.OPEN).build();
		Post p50 = Post.builder().userProfileInfo(upi6).type(PostType.COURSE).title("햇살과 바다의 도시 마이애미에서의 4일간의 열대 휴양").content("마이애미는 아름다운 해변과 독특한 문화적 분위기가 매력적인 도시입니다. 첫날은 사우스 비치에서 시작해 여유로운 바다 시간을 보내고, 저녁에는 아르 데코 디스트릭트를 탐험하며 예술적인 건축을 감상합니다. 둘째 날은 리틀 하바나에서 쿠바 문화를 경험하고, 비스케인 국립공원에서 스노클링과 보트 투어로 자연을 만끽합니다. 마지막 날은 윈우드 월즈의 거리 예술을 감상하며 마이애미의 현대적인 면모를 발견합니다.").country("미국").city("마이애미").viewCount(470L).likeCount(65L).status(PostStatus.OPEN).build();
		
		postRepo.save(p15);
		postRepo.save(p16);
		postRepo.save(p17);
		postRepo.save(p18);
		postRepo.save(p19);
		postRepo.save(p20);
		postRepo.save(p21);
		postRepo.save(p22);
		postRepo.save(p23);
		postRepo.save(p24);
		postRepo.save(p25);
		postRepo.save(p26);
		postRepo.save(p27);
		postRepo.save(p28);
		postRepo.save(p29);
		postRepo.save(p30);
		postRepo.save(p31);
		postRepo.save(p32);
		postRepo.save(p33);
		postRepo.save(p34);
		postRepo.save(p35);
		postRepo.save(p36);
		postRepo.save(p37);
		postRepo.save(p38);
		postRepo.save(p39);
		postRepo.save(p40);
		postRepo.save(p41);
		postRepo.save(p42);
		postRepo.save(p43);
		postRepo.save(p44);
		postRepo.save(p45);
		postRepo.save(p46);
		postRepo.save(p47);
		postRepo.save(p48);
		postRepo.save(p49);
		postRepo.save(p50);

		
		
		postRepo.save(p1);
		postRepo.save(p2);
		postRepo.save(p3);
		postRepo.save(p4);
		postRepo.save(p5);
		postRepo.save(p6);
		
		postRepo.save(p7);
		postRepo.save(p8); 
		postRepo.save(p9);
		postRepo.save(p10);
		postRepo.save(p11);
		postRepo.save(p12);
		postRepo.save(p13);
		postRepo.save(p14);

		LocalDateTime localTime = LocalDateTime.now(); 
		// 1. 채팅방(ChatRoom) 생성
		ChatRoom chatRoom1 = ChatRoom.builder().title("private Chat Room").type(ChatRoomType.ONE_ON_ONE).lastmessagetime(localTime).build();
		ChatRoom chatRoom2 = ChatRoom.builder().title("private Chat Room222").type(ChatRoomType.ONE_ON_ONE).lastmessagetime(localTime).build();
		ChatRoom groupChatRoom = ChatRoom.builder().title("Group Chat Room").type(ChatRoomType.GROUP).lastmessagetime(localTime).build();

		// 채팅방 저장
		chatRoomRepo.save(chatRoom1);  // 첫 번째 1:1 채팅방 저장
		chatRoomRepo.save(chatRoom2);  // 두 번째 1:1 채팅방 저장
		chatRoomRepo.save(groupChatRoom);  // 그룹 채팅방 저장

		// 채팅방 ID를 가져오기
		Long chatRoomId1 = chatRoom1.getId(); // 첫 번째 채팅방 ID
		Long chatRoomId2 = chatRoom2.getId(); // 두 번째 채팅방 ID
		Long groupChatRoomId = groupChatRoom.getId(); // 그룹 채팅방 ID

		// 2. 사용자 정보와 ChatParticipant 설정
		UserProfileInfo user1 = upi2; // 예시 사용자 1
		UserProfileInfo user2 = upi3; // 예시 사용자 2
		UserProfileInfo groupOwner = upi2; // 그룹 채팅방 소유자
		UserProfileInfo groupOwner2 = upi3; // 그룹 채팅방 소유자
		UserProfileInfo groupOwner3 = upi4; // 그룹 채팅방 소유자

		// 첫 번째 1:1 채팅방 참가자 (소유자)
		ChatParticipant participant1 = ChatParticipant.builder()
		        .userProfileInfo(upi2)
		        .chatRoomId(chatRoomId1)
		        .isOwner(true)  // 채팅방 소유자
		        .build();
		ChatParticipant participant2 = ChatParticipant.builder()
		        .userProfileInfo(upi3)
		        .chatRoomId(chatRoomId1)
		        .isOwner(false)  // 채팅방 소유자
		        .build();
		chatParticipantsRepo.save(participant1);  // 저장
		chatParticipantsRepo.save(participant2);  // 저장

		// 두 번째 1:1 채팅방 참가자
		ChatParticipant participant3 = ChatParticipant.builder()
		        .userProfileInfo(upi3)
		        .chatRoomId(chatRoomId2)
		        .isOwner(true)  // 일반 참가자
		        .build();
		ChatParticipant participant4 = ChatParticipant.builder()
		        .userProfileInfo(upi4)
		        .chatRoomId(chatRoomId2)
		        .isOwner(false)  // 채팅방 소유자
		        .build();
		chatParticipantsRepo.save(participant3);  // 저장
		chatParticipantsRepo.save(participant4);  // 저장

		// 그룹 채팅방 소유자 추가
		ChatParticipant groupOwnerParticipant = ChatParticipant.builder()
		        .userProfileInfo(groupOwner)
		        .chatRoomId(groupChatRoomId)
		        .isOwner(false)  // 그룹 채팅방 소유자
		        .build();
		ChatParticipant groupOwnerParticipant2 = ChatParticipant.builder()
		        .userProfileInfo(groupOwner2)
		        .chatRoomId(groupChatRoomId)
		        .isOwner(false)  // 그룹 채팅방 소유자
		        .build();
		ChatParticipant groupOwnerParticipant3 = ChatParticipant.builder()
		        .userProfileInfo(groupOwner3)
		        .chatRoomId(groupChatRoomId)
		        .isOwner(true)  // 그룹 채팅방 소유자
		        .build();
		
		chatParticipantsRepo.save(groupOwnerParticipant);  // 저장
		chatParticipantsRepo.save(groupOwnerParticipant2);  // 저장
		chatParticipantsRepo.save(groupOwnerParticipant3);  // 저장

		// 3. 채팅 메시지(ChatMessage) 미리 생성 및 저장
		ChatMessage message1 = ChatMessage.builder()
		        .message("안녕하세요! 첫 번째 메시지입니다.")
		        .chatRoomId(chatRoomId1)
		        .userProfileInfo(upi2)
		        .timestamp(new Date())
		        .build();

		ChatMessage message2 = ChatMessage.builder()
		        .message("안녕하세요! 두 번째 메시지입니다.")
		        .chatRoomId(chatRoomId2)
		        .userProfileInfo(upi3)
		        .timestamp(new Date())
		        .build();

		ChatMessage message3 = ChatMessage.builder()
		        .message("안녕하세요! 세 번째 메시지입니다.")
		        .chatRoomId(groupChatRoomId)
		        .userProfileInfo(upi2)
		        .timestamp(new Date())
		        .build();

		ChatMessage message4 = ChatMessage.builder()
		        .message("안녕하세요! 네 번째 메시지입니다.")
		        .chatRoomId(groupChatRoomId)
		        .userProfileInfo(upi3)
		        .timestamp(new Date())
		        .build();

		ChatMessage message5 = ChatMessage.builder()
		        .message("안녕하세요! 다섯 번째 메시지입니다.")
		        .chatRoomId(groupChatRoomId)
		        .userProfileInfo(upi4)
		        .timestamp(new Date())
		        .build();

		// 메시지 저장
		messageRepo.save(message1);
		messageRepo.save(message2);
		messageRepo.save(message3);
		messageRepo.save(message4);
		messageRepo.save(message5);

		// 4. 팔로우 데이터 생성 및 저장
		UserFollow follow1 = UserFollow.builder().followerUserProfileInfo(upi2).followeeUserProfileInfo(upi3).followType(FollowType.FOLLOW).build();
		UserFollow follow2 = UserFollow.builder().followerUserProfileInfo(upi3).followeeUserProfileInfo(upi2).followType(FollowType.FOLLOW).build();
		
		UserFollow follow3 = UserFollow.builder().followerUserProfileInfo(upi2).followeeUserProfileInfo(upi4).followType(FollowType.FOLLOW).build();
		UserFollow follow4 = UserFollow.builder().followerUserProfileInfo(upi4).followeeUserProfileInfo(upi2).followType(FollowType.FOLLOW).build();

		UserFollow follow5 = UserFollow.builder().followerUserProfileInfo(upi3).followeeUserProfileInfo(upi4).followType(FollowType.FOLLOW).build();
		UserFollow follow6 = UserFollow.builder().followerUserProfileInfo(upi4).followeeUserProfileInfo(upi3).followType(FollowType.FOLLOW).build();
		
		followRepo.save(follow1);
		followRepo.save(follow2);
		followRepo.save(follow3);
		followRepo.save(follow4);
		followRepo.save(follow5);
		followRepo.save(follow6);

		// 5. 신고 데이터 생성 및 저장
		ReportToUser report1 = ReportToUser.builder().reporterId(u1.getId()).content("2번 유저를 신고").reportedUser(u2).build();
        ReportToUser report2 = ReportToUser.builder().reporterId(u2.getId()).content("3번 유저를 신고").reportedUser(u3).build();
        ReportToUser report3 = ReportToUser.builder().reporterId(u3.getId()).content("4번 유저를 신고").reportedUser(u4).build();
        ReportToUser report4 = ReportToUser.builder().reporterId(u4.getId()).content("5번 유저를 신고").reportedUser(u5).build();
        ReportToUser report5 = ReportToUser.builder().reporterId(u5.getId()).content("6번 유저를 신고").reportedUser(u6).build();
        ReportToUser report6 = ReportToUser.builder().reporterId(u6.getId()).content("7번 유저를 신고").reportedUser(u7).build();
        ReportToUser report7 = ReportToUser.builder().reporterId(u7.getId()).content("8번 유저를 신고").reportedUser(u8).build();
        ReportToUser report8 = ReportToUser.builder().reporterId(u8.getId()).content("9번 유저를 신고").reportedUser(u9).build();
        ReportToUser report9 = ReportToUser.builder().reporterId(u9.getId()).content("10번 유저를 신고").reportedUser(u10).build();
		ReportToUser report10 = ReportToUser.builder().reporterId(u10.getId()).content("1번 유저를 신고").reportedUser(u1).build();
        
		reportRepo.save(report1);
        reportRepo.save(report2);
        reportRepo.save(report3);
        reportRepo.save(report4);
        reportRepo.save(report5);
        reportRepo.save(report6);
        reportRepo.save(report7);
        reportRepo.save(report8);
        reportRepo.save(report9);
        reportRepo.save(report10);

		ReportToPost report11 = ReportToPost.builder().reporterId(u1.getId()).content("1번 게시글을 신고").reportedPost(p1).build();
		ReportToPost report12 = ReportToPost.builder().reporterId(u2.getId()).content("2번 게시글을 신고").reportedPost(p2).build();
		ReportToPost report13 = ReportToPost.builder().reporterId(u3.getId()).content("3번 게시글을 신고").reportedPost(p3).build();
		ReportToPost report14 = ReportToPost.builder().reporterId(u4.getId()).content("4번 게시글을 신고").reportedPost(p4).build();
		ReportToPost report15 = ReportToPost.builder().reporterId(u5.getId()).content("5번 게시글을 신고").reportedPost(p5).build();
		ReportToPost report16 = ReportToPost.builder().reporterId(u6.getId()).content("6번 게시글을 신고").reportedPost(p6).build();
		ReportToPost report17 = ReportToPost.builder().reporterId(u7.getId()).content("7번 게시글을 신고").reportedPost(p7).build();
		ReportToPost report18 = ReportToPost.builder().reporterId(u8.getId()).content("8번 게시글을 신고").reportedPost(p8).build();
		ReportToPost report19 = ReportToPost.builder().reporterId(u9.getId()).content("9번 게시글을 신고").reportedPost(p9).build();
		ReportToPost report20 = ReportToPost.builder().reporterId(u10.getId()).content("10번 게시글을 신고").reportedPost(p10).build();
		ReportToPost report21 = ReportToPost.builder().reporterId(u10.getId()).content("11번 게시글을 신고").reportedPost(p11).build();

		reportRepo.save(report11);
		reportRepo.save(report12);
		reportRepo.save(report13);
		reportRepo.save(report14);
		reportRepo.save(report15);
		reportRepo.save(report16);
		reportRepo.save(report17);
		reportRepo.save(report18);
		reportRepo.save(report19);
		reportRepo.save(report20);
		reportRepo.save(report21);

		ReportToChatRoom report22 = ReportToChatRoom.builder().reporterId(u1.getId()).content("1번 채팅방을 신고").reportedChatRoom(chatRoom1).build();
		ReportToChatRoom report23 = ReportToChatRoom.builder().reporterId(u2.getId()).content("2번 채팅방을 신고").reportedChatRoom(chatRoom2).build();
		ReportToChatRoom report24 = ReportToChatRoom.builder().reporterId(u3.getId()).content("3번 채팅방을 신고").reportedChatRoom(groupChatRoom).build();

		reportRepo.save(report22);
		reportRepo.save(report23);
		reportRepo.save(report24);

		ReportToChatMessage report25 = ReportToChatMessage.builder().reporterId(u1.getId()).content("1번 채팅 메시지를 신고").reportedChatMessage(message1).build();
		ReportToChatMessage report26 = ReportToChatMessage.builder().reporterId(u2.getId()).content("2번 채팅 메시지를 신고").reportedChatMessage(message2).build();
		ReportToChatMessage report27 = ReportToChatMessage.builder().reporterId(u3.getId()).content("3번 채팅 메시지를 신고").reportedChatMessage(message3).build();
		ReportToChatMessage report28 = ReportToChatMessage.builder().reporterId(u4.getId()).content("4번 채팅 메시지를 신고").reportedChatMessage(message4).build();
		ReportToChatMessage report29 = ReportToChatMessage.builder().reporterId(u5.getId()).content("5번 채팅 메시지를 신고").reportedChatMessage(message5).build();

		reportRepo.save(report25);
		reportRepo.save(report26);
		reportRepo.save(report27);
		reportRepo.save(report28);
		reportRepo.save(report29);

        // 확인용 출력
        System.out.println("초기 데이터 저장 완료!");

        System.out.println("\n\n### ### ### ### ### ### ### ###");
        System.out.println("#                             #");
		System.out.println("#   서버가 성공적으로 실행되었습니다.   #");
		System.out.println("#                             #");
		System.out.println("### ### ### ### ### ### ### ###");
	}
}