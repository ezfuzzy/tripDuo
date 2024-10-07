package com.example.tripDuo;

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
import com.example.tripDuo.entity.User;
import com.example.tripDuo.entity.UserProfileInfo;
import com.example.tripDuo.enums.AccountStatus;
import com.example.tripDuo.enums.ChatRoomType;
import com.example.tripDuo.enums.Gender;
import com.example.tripDuo.enums.PostStatus;
import com.example.tripDuo.enums.PostType;
import com.example.tripDuo.enums.UserRole;
import com.example.tripDuo.enums.VerificationStatus;
import com.example.tripDuo.repository.ChatMessageRepository;
import com.example.tripDuo.repository.ChatParticipantRepository;
import com.example.tripDuo.repository.ChatRoomRepository;
import com.example.tripDuo.repository.PostRepository;
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
	private PasswordEncoder encoder;
	
	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		System.out.println("\n### init ###");
		
		String[] socialLinks = {"tictok+", "instagram+"};
		
		User u1 = User.builder().username("ezfz").password(encoder.encode("hysz")).phoneNumber("010-3456-7890").role(UserRole.ADMIN).build(); 
		User savedUser1 = userRepo.save(u1);
		UserProfileInfo upi1 = UserProfileInfo.builder().user(savedUser1).nickname("ezfz").age((long) 28).profilePicture("9a926641-7e7f-4d23-8a78-1fe301813ccd.jpg").socialLinks(socialLinks).gender(Gender.MALE).build();
		upi1 = userProfileInfoRepo.save(upi1);
		
		User u2 = User.builder().username("aaaa").password(encoder.encode("aaaa")).phoneNumber("010-3456-7890").role(UserRole.USER).build();
		User savedUser2 = userRepo.save(u2);
		UserProfileInfo upi2 = UserProfileInfo.builder().user(savedUser2).nickname("a5").profilePicture("e68b541e-8fb9-4d13-8358-6d2111303fa6.png").gender(Gender.MALE).build();
		upi2 = userProfileInfoRepo.save(upi2);

		User u3 = User.builder().username("bbbb").password(encoder.encode("bbbb")).phoneNumber("010-3456-7890").role(UserRole.USER).build();
		User savedUser3 = userRepo.save(u3);
		UserProfileInfo upi3 = UserProfileInfo.builder().user(savedUser3).nickname("b5").gender(Gender.FEMALE).build();
		upi3 = userProfileInfoRepo.save(upi3);

		User u4 = User.builder().username("cccc").password(encoder.encode("cccc")).phoneNumber("010-3456-7890").role(UserRole.USER).build();
		User savedUser4 = userRepo.save(u4);
		UserProfileInfo upi4 = UserProfileInfo.builder().user(savedUser4).nickname("c5").gender(Gender.FEMALE).build();
		upi4 = userProfileInfoRepo.save(upi4);

		User u5 = User.builder().username("dddd").password(encoder.encode("dddd")).phoneNumber("010-3456-7890").role(UserRole.USER).build();
		User savedUser5 = userRepo.save(u5);
		UserProfileInfo upi5 = UserProfileInfo.builder().user(savedUser5).nickname("d5").gender(Gender.MALE).build();
		upi5 = userProfileInfoRepo.save(upi5);

		User u6 = User.builder().username("user1").password(encoder.encode("password1")).phoneNumber("010-1234-5678").email("user1@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser6 = userRepo.save(u6);
		UserProfileInfo upi6 = UserProfileInfo.builder().user(savedUser6).nickname("닉네임1").age((long) 25).ratings((long) 1300).gender(Gender.MALE).profilePicture("e68b541e-8fb9-4d13-8358-6d2111303fa6.png").profileMessage("안녕하세요1").curLocation("서울").build();
		upi6 = userProfileInfoRepo.save(upi6);

		User u7 = User.builder().username("user2").password(encoder.encode("password2")).phoneNumber("010-2345-6789").email("user2@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser7 = userRepo.save(u7);
		UserProfileInfo upi7 = UserProfileInfo.builder().user(savedUser7).nickname("닉네임2").age((long) 30).ratings((long) 1600).gender(Gender.MALE).profileMessage("안녕하세요2").curLocation("부산").build();
		upi7 = userProfileInfoRepo.save(upi7);

		User u8 = User.builder().username("user3").password(encoder.encode("password3")).phoneNumber("010-3456-7890").email("user3@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser8 = userRepo.save(u8);
		UserProfileInfo upi8 = UserProfileInfo.builder().user(savedUser8).nickname("닉네임3").age((long) 22).ratings((long) 3200).gender(Gender.FEMALE).profileMessage("안녕하세요3").curLocation("대구").build();
		upi8 = userProfileInfoRepo.save(upi8);

		User u9 = User.builder().username("user4").password(encoder.encode("password4")).phoneNumber("010-4567-8901").email("user4@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser9 = userRepo.save(u9);
		UserProfileInfo upi9 = UserProfileInfo.builder().user(savedUser9).nickname("닉네임4").age((long) 28).ratings((long) 4600).gender(Gender.MALE).profileMessage("안녕하세요4").curLocation("인천").build();
		upi9 = userProfileInfoRepo.save(upi9);

		User u10 = User.builder().username("user5").password(encoder.encode("password5")).phoneNumber("010-5678-9012").email("user5@example.com").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).role(UserRole.USER).build();
		User savedUser10 = userRepo.save(u10);
		UserProfileInfo upi10 = UserProfileInfo.builder().user(savedUser10).nickname("닉네임5").age((long) 35).ratings((long) 6700).gender(Gender.MALE).profileMessage("안녕하세요5").curLocation("광주").build();
		upi10 = userProfileInfoRepo.save(upi10);

		
		Post p1 = Post.builder().userProfileInfo(upi6).type(PostType.MATE).title("제목입니다 111").content("내용입니다 111").country("한국").city("강릉").viewCount(123L).likeCount(18L).rating(81F).status(PostStatus.OPEN).build();
		Post p2 = Post.builder().userProfileInfo(upi7).type(PostType.MATE).title("제목입니다 222").content("내용입니다 222").country("베트남").city("하노이").viewCount(234L).likeCount(28L).rating(23F).status(PostStatus.OPEN).build();
		Post p3 = Post.builder().userProfileInfo(upi8).type(PostType.MATE).title("제목입니다 333").content("내용입니다 333").country("태국").city("방콕").viewCount(456L).likeCount(38L).rating(45F).status(PostStatus.CLOSED).build();
		Post p4 = Post.builder().userProfileInfo(upi9).type(PostType.MATE).title("제목입니다 444").content("내용입니다 444").country("영국").city("런던").viewCount(345L).likeCount(78L).rating(88F).status(PostStatus.CLOSED).build();
		Post p5 = Post.builder().userProfileInfo(upi10).type(PostType.MATE).title("제목입니다 555").content("내용입니다 555").country("프랑스").city("파리").viewCount(567L).likeCount(128L).rating(96F).status(PostStatus.OPEN).build();
		Post p6 = Post.builder().userProfileInfo(upi6).type(PostType.MATE).title("제목입니다 666").content("내용입니다 666").country("스페인").city("바르셀로나").viewCount(789L).likeCount(98L).rating(72F).status(PostStatus.CLOSED).build();
		
		Post p7 = Post.builder().userProfileInfo(upi6).type(PostType.COURSE).title("course 1").content("내용입니다 666").country("스페인").city("바르셀로나").status(PostStatus.CLOSED).build();
		Post p8 = Post.builder().userProfileInfo(upi7).type(PostType.COURSE).title("course 2").content("내용입니다 666").country("스페인").city("바르셀로나").viewCount(789L).likeCount(98L).rating(72F).status(PostStatus.CLOSED).build();
		Post p9 = Post.builder().userProfileInfo(upi8).type(PostType.COURSE).title("course 3").content("내용입니다 666").country("스페인").city("바르셀로나").viewCount(789L).likeCount(98L).rating(72F).status(PostStatus.DELETED).build();
		Post p10 = Post.builder().userProfileInfo(upi9).type(PostType.COURSE).title("course 4").content("내용입니다 666").country("스페인").city("바르셀로나").viewCount(789L).likeCount(98L).rating(72F).status(PostStatus.CLOSED).build();
		Post p11 = Post.builder().userProfileInfo(upi10).type(PostType.COURSE).title("course 5").content("내용입니다 666").country("스페인").city("바르셀로나").viewCount(789L).likeCount(98L).rating(72F).status(PostStatus.CLOSED).build();
		
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
		
		// 1. 채팅방(ChatRoom) 생성
		ChatRoom chatRoom1 = ChatRoom.builder().title("private Chat Room").type(ChatRoomType.ONE_ON_ONE).build();
		ChatRoom chatRoom2 = ChatRoom.builder().title("private Chat Room222").type(ChatRoomType.ONE_ON_ONE).build();
		ChatRoom groupChatRoom = ChatRoom.builder().title("Group Chat Room").type(ChatRoomType.GROUP).build();

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

        
        // 확인용 출력
        System.out.println("초기 데이터 저장 완료!");

        System.out.println("\n\n### ### ### ### ### ### ### ###");
        System.out.println("#                             #");
		System.out.println("#   서버가 성공적으로 실행되었습니다.   #");
		System.out.println("#                             #");
		System.out.println("### ### ### ### ### ### ### ###");
	}
	
	


}