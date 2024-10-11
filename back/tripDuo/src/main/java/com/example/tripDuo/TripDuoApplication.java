package com.example.tripDuo;

import java.time.LocalDateTime;
import java.util.Date;

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
	
	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		System.out.println("\n### init ###");
		
		String[] socialLinks = {"tictok+", "instagram+"};
		
		User u1 = User.builder().username("ezfz").password(encoder.encode("hysz")).encryptedPhoneNumber("010-3456-7890").role(UserRole.ADMIN).build(); 
		User savedUser1 = userRepo.save(u1);
		UserProfileInfo upi1 = UserProfileInfo.builder().user(savedUser1).nickname("ezfz").age((long) 28).profilePicture("9a926641-7e7f-4d23-8a78-1fe301813ccd.jpg").socialLinks(socialLinks).gender(Gender.MALE).build();
		upi1 = userProfileInfoRepo.save(upi1);
		
		User u2 = User.builder().username("aaaa").password(encoder.encode("aaaa")).encryptedPhoneNumber("010-3456-7890").role(UserRole.USER).build();
		User savedUser2 = userRepo.save(u2);
		UserProfileInfo upi2 = UserProfileInfo.builder().user(savedUser2).nickname("a5").profilePicture("e68b541e-8fb9-4d13-8358-6d2111303fa6.png").gender(Gender.MALE).build();
		upi2 = userProfileInfoRepo.save(upi2);

		User u3 = User.builder().username("bbbb").password(encoder.encode("bbbb")).encryptedPhoneNumber("010-3456-7890").role(UserRole.USER).build();
		User savedUser3 = userRepo.save(u3);
		UserProfileInfo upi3 = UserProfileInfo.builder().user(savedUser3).nickname("b5").gender(Gender.FEMALE).build();
		upi3 = userProfileInfoRepo.save(upi3);

		User u4 = User.builder().username("cccc").password(encoder.encode("cccc")).encryptedPhoneNumber("010-3456-7890").role(UserRole.USER).build();
		User savedUser4 = userRepo.save(u4);
		UserProfileInfo upi4 = UserProfileInfo.builder().user(savedUser4).nickname("c5").gender(Gender.FEMALE).build();
		upi4 = userProfileInfoRepo.save(upi4);

		User u5 = User.builder().username("dddd").password(encoder.encode("dddd")).encryptedPhoneNumber("010-3456-7890").role(UserRole.USER).build();
		User savedUser5 = userRepo.save(u5);
		UserProfileInfo upi5 = UserProfileInfo.builder().user(savedUser5).nickname("d5").gender(Gender.MALE).build();
		upi5 = userProfileInfoRepo.save(upi5);

		User u6 = User.builder().username("user1").password(encoder.encode("password1")).encryptedPhoneNumber("010-1234-5678").email("user1@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser6 = userRepo.save(u6);
		UserProfileInfo upi6 = UserProfileInfo.builder().user(savedUser6).nickname("닉네임1").age((long) 25).ratings((long) 1300).gender(Gender.MALE).profilePicture("e68b541e-8fb9-4d13-8358-6d2111303fa6.png").profileMessage("안녕하세요1").curLocation("서울").build();
		upi6 = userProfileInfoRepo.save(upi6);

		User u7 = User.builder().username("user2").password(encoder.encode("password2")).encryptedPhoneNumber("010-2345-6789").email("user2@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser7 = userRepo.save(u7);
		UserProfileInfo upi7 = UserProfileInfo.builder().user(savedUser7).nickname("닉네임2").age((long) 30).ratings((long) 1600).gender(Gender.MALE).profileMessage("안녕하세요2").curLocation("부산").build();
		upi7 = userProfileInfoRepo.save(upi7);

		User u8 = User.builder().username("user3").password(encoder.encode("password3")).encryptedPhoneNumber("010-3456-7890").email("user3@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser8 = userRepo.save(u8);
		UserProfileInfo upi8 = UserProfileInfo.builder().user(savedUser8).nickname("닉네임3").age((long) 22).ratings((long) 3200).gender(Gender.FEMALE).profileMessage("안녕하세요3").curLocation("대구").build();
		upi8 = userProfileInfoRepo.save(upi8);

		User u9 = User.builder().username("user4").password(encoder.encode("password4")).encryptedPhoneNumber("010-4567-8901").email("user4@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser9 = userRepo.save(u9);
		UserProfileInfo upi9 = UserProfileInfo.builder().user(savedUser9).nickname("닉네임4").age((long) 28).ratings((long) 4600).gender(Gender.MALE).profileMessage("안녕하세요4").curLocation("인천").build();
		upi9 = userProfileInfoRepo.save(upi9);

		User u10 = User.builder().username("user5").password(encoder.encode("password5")).encryptedPhoneNumber("010-5678-9012").email("user5@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser10 = userRepo.save(u10);
		UserProfileInfo upi10 = UserProfileInfo.builder().user(savedUser10).nickname("닉네임5").age((long) 35).ratings((long) 6700).gender(Gender.MALE).profileMessage("안녕하세요5").curLocation("광주").build();
		upi10 = userProfileInfoRepo.save(upi10);

		
		Post p1 = Post.builder().userProfileInfo(upi6).type(PostType.MATE).title("메이트 구해용").content("내용입니다 111").country("대한민국").city("제주").viewCount(123L).likeCount(18L).status(PostStatus.OPEN).build();
		Post p2 = Post.builder().userProfileInfo(upi7).type(PostType.MATE).title("착한 메이트 구해용").content("내용입니다 222").country("미국").city("로스앤젤레스").viewCount(234L).likeCount(28L).status(PostStatus.OPEN).build();
		Post p3 = Post.builder().userProfileInfo(upi8).type(PostType.MATE).title("러시아 동행하실분").content("내용입니다 333").country("러시아").city("모스크바").viewCount(456L).likeCount(38L).status(PostStatus.CLOSED).build();
		Post p4 = Post.builder().userProfileInfo(upi9).type(PostType.MATE).title("영국에서 피쉬앤 칩스 드실분").content("내용입니다 444").country("영국").city("런던").viewCount(345L).likeCount(78L).status(PostStatus.CLOSED).build();
		Post p5 = Post.builder().userProfileInfo(upi10).type(PostType.MATE).title("같이 와인밭 가보실분ㅜㅜ 패키지라 2인 이상 필수에요").content("내용입니다 555").country("프랑스").city("파리").viewCount(567L).likeCount(128L).status(PostStatus.OPEN).build();
		Post p6 = Post.builder().userProfileInfo(upi6).type(PostType.MATE).title("스페인 혼자가는데 무서워서 같이 가실 메이트 구해요").content("내용입니다 666").country("스페인").city("바르셀로나").viewCount(789L).likeCount(98L).status(PostStatus.CLOSED).build();
		
		Post p7 = Post.builder().userProfileInfo(upi6).type(PostType.COURSE).title("course 1").content("내용입니다 666").country("중국").city("상하이").status(PostStatus.CLOSED).build();
		Post p8 = Post.builder().userProfileInfo(upi7).type(PostType.COURSE).title("course 2").content("내용입니다 666").country("영국").city("런던").viewCount(789L).likeCount(98L).rating(4.5F).status(PostStatus.CLOSED).build();
		Post p9 = Post.builder().userProfileInfo(upi8).type(PostType.COURSE).title("course 3").content("내용입니다 666").country("프랑스").city("파리").viewCount(789L).likeCount(98L).rating(3.3F).status(PostStatus.DELETED).build();
		Post p10 = Post.builder().userProfileInfo(upi9).type(PostType.COURSE).title("course 4").content("내용입니다 666").country("미국").city("로스앤젤레스").viewCount(789L).likeCount(98L).rating(4.7F).status(PostStatus.CLOSED).build();
		Post p11 = Post.builder().userProfileInfo(upi10).type(PostType.COURSE).title("course 5").content("내용입니다 666").country("호주").city("멜버른").viewCount(789L).likeCount(98L).rating(4.8F).status(PostStatus.CLOSED).build();
		Post p12 = Post.builder().userProfileInfo(upi10).type(PostType.COURSE).title("course 6").content("내용입니다 12").country("대한민국").city("인천").viewCount(789L).likeCount(98L).rating(3.6F).status(PostStatus.CLOSED).build();
		Post p13 = Post.builder().userProfileInfo(upi10).type(PostType.COURSE).title("course 7").content("내용입니다 13").country("대한민국").city("제주").viewCount(789L).likeCount(98L).rating(4.9F).status(PostStatus.CLOSED).build();
		Post p14 = Post.builder().userProfileInfo(upi10).type(PostType.COURSE).title("course 8").content("내용입니다 14").country("대한민국").city("서울").viewCount(789L).likeCount(98L).rating(3.9F).status(PostStatus.CLOSED).build();
		Post p15 = Post.builder().userProfileInfo(upi6).type(PostType.COURSE).title("도쿄에서 여행 메이트 구해요").content("도쿄 여행 같이 하실 분").country("일본").city("도쿄").viewCount(450L).likeCount(55L).status(PostStatus.OPEN).build();
		Post p16 = Post.builder().userProfileInfo(upi2).type(PostType.COURSE).title("오사카 여행 메이트 구합니다").content("오사카 같이 여행해요").country("일본").city("오사카").viewCount(320L).likeCount(40L).status(PostStatus.OPEN).build();
		Post p17 = Post.builder().userProfileInfo(upi3).type(PostType.COURSE).title("교토 여행 파트너 찾습니다").content("교토에서 같이 여행하실 분").country("일본").city("교토").viewCount(570L).likeCount(65L).status(PostStatus.OPEN).build();
		Post p18 = Post.builder().userProfileInfo(upi4).type(PostType.COURSE).title("삿포로에서 메이트 모집").content("삿포로 여행 메이트 구해요").country("일본").city("삿포로").viewCount(420L).likeCount(50L).status(PostStatus.OPEN).build();

		Post p19 = Post.builder().userProfileInfo(upi5).type(PostType.COURSE).title("베이징에서 함께 여행할 메이트").content("베이징에서 같이 하실 분 찾습니다").country("중국").city("베이징").viewCount(300L).likeCount(35L).status(PostStatus.OPEN).build();
		Post p20 = Post.builder().userProfileInfo(upi6).type(PostType.COURSE).title("상하이 여행 메이트 구해요").content("상하이에서 여행 메이트 구합니다").country("중국").city("상하이").viewCount(510L).likeCount(75L).status(PostStatus.OPEN).build();
		Post p21 = Post.builder().userProfileInfo(upi7).type(PostType.COURSE).title("광저우에서 여행 메이트 구합니다").content("광저우 같이 여행해요").country("중국").city("광저우").viewCount(280L).likeCount(45L).status(PostStatus.OPEN).build();
		Post p22 = Post.builder().userProfileInfo(upi8).type(PostType.COURSE).title("시안에서 여행 메이트 구해요").content("시안에서 같이 여행하실 분 찾습니다").country("중국").city("시안").viewCount(410L).likeCount(60L).status(PostStatus.OPEN).build();

		Post p23 = Post.builder().userProfileInfo(upi9).type(PostType.COURSE).title("델리 여행 메이트 찾습니다").content("델리에서 같이 여행할 분").country("인도").city("델리").viewCount(530L).likeCount(80L).status(PostStatus.OPEN).build();
		Post p24 = Post.builder().userProfileInfo(upi10).type(PostType.COURSE).title("뭄바이에서 메이트 구해요").content("뭄바이 여행 메이트 구합니다").country("인도").city("뭄바이").viewCount(460L).likeCount(70L).status(PostStatus.OPEN).build();
		Post p25 = Post.builder().userProfileInfo(upi6).type(PostType.COURSE).title("콜카타에서 메이트 구해요").content("콜카타에서 함께 여행하실 분").country("인도").city("콜카타").viewCount(400L).likeCount(65L).status(PostStatus.OPEN).build();
		Post p26 = Post.builder().userProfileInfo(upi7).type(PostType.COURSE).title("벵갈루루에서 여행 메이트 찾습니다").content("벵갈루루에서 여행 메이트 구합니다").country("인도").city("벵갈루루").viewCount(320L).likeCount(40L).status(PostStatus.OPEN).build();

		Post p27 = Post.builder().userProfileInfo(upi8).type(PostType.COURSE).title("바르셀로나에서 메이트 구합니다").content("바르셀로나 여행 같이 가실 분 찾습니다").country("스페인").city("바르셀로나").viewCount(510L).likeCount(90L).status(PostStatus.OPEN).build();
		Post p28 = Post.builder().userProfileInfo(upi9).type(PostType.COURSE).title("그라나다에서 여행 메이트 구해요").content("그라나다 여행 메이트 구합니다").country("스페인").city("그라나다").viewCount(470L).likeCount(80L).status(PostStatus.OPEN).build();
		Post p29 = Post.builder().userProfileInfo(upi6).type(PostType.COURSE).title("마드리드 여행 메이트 모집").content("마드리드에서 함께할 분 찾습니다").country("스페인").city("마드리드").viewCount(340L).likeCount(50L).status(PostStatus.OPEN).build();
		Post p30 = Post.builder().userProfileInfo(upi7).type(PostType.COURSE).title("세비야 여행 파트너 구합니다").content("세비야에서 메이트 구해요").country("스페인").city("세비야").viewCount(290L).likeCount(45L).status(PostStatus.OPEN).build();

		Post p31 = Post.builder().userProfileInfo(upi8).type(PostType.COURSE).title("런던에서 메이트 구해요").content("런던 여행 같이 하실 분").country("영국").city("런던").viewCount(580L).likeCount(85L).status(PostStatus.OPEN).build();
		Post p32 = Post.builder().userProfileInfo(upi9).type(PostType.COURSE).title("맨체스터 여행 메이트 구합니다").content("맨체스터에서 메이트 찾습니다").country("영국").city("맨체스터").viewCount(350L).likeCount(55L).status(PostStatus.OPEN).build();
		Post p33 = Post.builder().userProfileInfo(upi5).type(PostType.COURSE).title("버밍엄 여행 메이트 구해요").content("버밍엄에서 여행 메이트 구합니다").country("영국").city("버밍엄").viewCount(370L).likeCount(60L).status(PostStatus.OPEN).build();
		Post p34 = Post.builder().userProfileInfo(upi2).type(PostType.COURSE).title("리버풀에서 여행 메이트 찾습니다").content("리버풀 여행 같이 가실 분").country("영국").city("리버풀").viewCount(430L).likeCount(70L).status(PostStatus.OPEN).build();

		Post p35 = Post.builder().userProfileInfo(upi1).type(PostType.COURSE).title("베를린에서 메이트 구해요").content("베를린에서 함께 여행하실 분 찾습니다").country("독일").city("베를린").viewCount(620L).likeCount(95L).status(PostStatus.OPEN).build();
		Post p36 = Post.builder().userProfileInfo(upi2).type(PostType.COURSE).title("뮌헨에서 여행 메이트 모집").content("뮌헨에서 여행 메이트 구해요").country("독일").city("뮌헨").viewCount(520L).likeCount(85L).status(PostStatus.OPEN).build();
		Post p37 = Post.builder().userProfileInfo(upi3).type(PostType.COURSE).title("프랑크푸르트 여행 메이트 구합니다").content("프랑크푸르트 여행 메이트 구합니다").country("독일").city("프랑크푸르트").viewCount(490L).likeCount(80L).status(PostStatus.OPEN).build();
		Post p38 = Post.builder().userProfileInfo(upi4).type(PostType.COURSE).title("함부르크에서 메이트 구합니다").content("함부르크에서 함께할 분 구해요").country("독일").city("함부르크").viewCount(470L).likeCount(75L).status(PostStatus.OPEN).build();

		Post p39 = Post.builder().userProfileInfo(upi5).type(PostType.COURSE).title("파리 여행 메이트 구합니다").content("파리에서 함께 여행하실 분").country("프랑스").city("파리").viewCount(640L).likeCount(100L).status(PostStatus.OPEN).build();
		Post p40 = Post.builder().userProfileInfo(upi6).type(PostType.COURSE).title("마르세유에서 메이트 구해요").content("마르세유에서 여행 메이트 구합니다").country("프랑스").city("마르세유").viewCount(420L).likeCount(60L).status(PostStatus.OPEN).build();
		Post p41 = Post.builder().userProfileInfo(upi7).type(PostType.COURSE).title("리옹 여행 메이트 모집").content("리옹에서 메이트 구합니다").country("프랑스").city("리옹").viewCount(460L).likeCount(70L).status(PostStatus.OPEN).build();
		Post p42 = Post.builder().userProfileInfo(upi8).type(PostType.COURSE).title("니스에서 메이트 찾습니다").content("니스에서 여행 메이트 구해요").country("프랑스").city("니스").viewCount(430L).likeCount(65L).status(PostStatus.OPEN).build();

		Post p43 = Post.builder().userProfileInfo(upi9).type(PostType.COURSE).title("로마에서 메이트 구해요").content("로마에서 여행 메이트 구합니다").country("이탈리아").city("로마").viewCount(540L).likeCount(85L).status(PostStatus.OPEN).build();
		Post p44 = Post.builder().userProfileInfo(upi3).type(PostType.COURSE).title("밀라노에서 메이트 구합니다").content("밀라노에서 함께 하실 분 찾습니다").country("이탈리아").city("밀라노").viewCount(370L).likeCount(55L).status(PostStatus.OPEN).build();
		Post p45 = Post.builder().userProfileInfo(upi1).type(PostType.COURSE).title("베네치아 여행 메이트 구해요").content("베네치아에서 여행 메이트 구합니다").country("이탈리아").city("베네치아").viewCount(500L).likeCount(80L).status(PostStatus.OPEN).build();
		Post p46 = Post.builder().userProfileInfo(upi2).type(PostType.COURSE).title("피렌체에서 여행 메이트 찾습니다").content("피렌체에서 여행 메이트 구해요").country("이탈리아").city("피렌체").viewCount(390L).likeCount(60L).status(PostStatus.OPEN).build();

		Post p47 = Post.builder().userProfileInfo(upi3).type(PostType.COURSE).title("뉴욕에서 여행 메이트 구해요").content("뉴욕에서 함께 하실 분 찾습니다").country("미국").city("뉴욕").viewCount(620L).likeCount(90L).status(PostStatus.OPEN).build();
		Post p48 = Post.builder().userProfileInfo(upi4).type(PostType.COURSE).title("로스앤젤레스에서 메이트 구해요").content("로스앤젤레스 여행 메이트 구해요").country("미국").city("로스앤젤레스").viewCount(560L).likeCount(85L).status(PostStatus.OPEN).build();
		Post p49 = Post.builder().userProfileInfo(upi5).type(PostType.COURSE).title("시카고에서 메이트 구합니다").content("시카고에서 메이트 구해요").country("미국").city("시카고").viewCount(490L).likeCount(70L).status(PostStatus.OPEN).build();
		Post p50 = Post.builder().userProfileInfo(upi6).type(PostType.COURSE).title("마이애미에서 메이트 모집").content("마이애미에서 메이트 구합니다").country("미국").city("마이애미").viewCount(470L).likeCount(65L).status(PostStatus.OPEN).build();
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