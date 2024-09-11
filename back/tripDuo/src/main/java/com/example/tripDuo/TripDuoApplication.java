package com.example.tripDuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.tripDuo.entity.Post;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.enums.AccountStatus;
import com.example.tripDuo.enums.Gender;
import com.example.tripDuo.enums.PostStatus;
import com.example.tripDuo.enums.PostType;
import com.example.tripDuo.enums.UserRole;
import com.example.tripDuo.enums.VerificationStatus;
import com.example.tripDuo.repository.PostRepository;
import com.example.tripDuo.repository.UserRepository;

@PropertySource(value = "classpath:custom.properties")
@SpringBootApplication
public class TripDuoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripDuoApplication.class, args);
	}
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PostRepository postRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		System.out.println("\n### init ###");
		User u1 = User.builder().username("ezfz").password(encoder.encode("hysz")).gender(Gender.MALE).role(UserRole.ADMIN).build(); 
		User u2 = User.builder().username("aaaa").password(encoder.encode("aaaa")).gender(Gender.MALE).role(UserRole.USER).build();
		User u3 = User.builder().username("bbbb").password(encoder.encode("bbbb")).gender(Gender.FEMALE).role(UserRole.USER).build();
		User u4 = User.builder().username("cccc").password(encoder.encode("cccc")).gender(Gender.FEMALE).role(UserRole.USER).build();
		User u5 = User.builder().username("dddd").password(encoder.encode("dddd")).gender(Gender.MALE).role(UserRole.USER).build();
		
		User t1 = User.builder().username("user1").password(encoder.encode("password1")).nickname("닉네임1").age((long) 25).gender(Gender.MALE).phoneNumber("010-1234-5678").email("user1@example.com").profilePicture("b1e1f201-d99f-42df-b855-9f70b58bfbc2.png").profileMessage("안녕하세요1").curLocation("서울").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).socialLinks("http://social1.com").role(UserRole.USER).ratings(4.5f).lastLogin("5분 전").build();
		User t2 = User.builder().username("user2").password(encoder.encode("password2")).nickname("닉네임2").age((long) 30).gender(Gender.MALE).phoneNumber("010-2345-6789").email("user2@example.com").profileMessage("안녕하세요2").curLocation("부산").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).socialLinks("http://social2.com").role(UserRole.USER).ratings(3.8f).lastLogin("10분 전").build();
		User t3 = User.builder().username("user3").password(encoder.encode("password3")).nickname("닉네임3").age((long) 22).gender(Gender.FEMALE).phoneNumber("010-3456-7890").email("user3@example.com").profileMessage("안녕하세요3").curLocation("대구").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).socialLinks("http://social3.com").role(UserRole.USER).ratings(4.2f).lastLogin("15분 전").build();
		User t4 = User.builder().username("user4").password(encoder.encode("password4")).nickname("닉네임4").age((long) 28).gender(Gender.MALE).phoneNumber("010-4567-8901").email("user4@example.com").profileMessage("안녕하세요4").curLocation("인천").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).socialLinks("http://social4.com").role(UserRole.USER).ratings(4.0f).lastLogin("20분 전").build();
		User t5 = User.builder().username("user5").password(encoder.encode("password5")).nickname("닉네임5").age((long) 35).gender(Gender.MALE).phoneNumber("010-5678-9012").email("user5@example.com").profileMessage("안녕하세요5").curLocation("광주").verificationStatus(VerificationStatus.VERIFIED).accountStatus(AccountStatus.ACTIVE).socialLinks("http://social5.com").role(UserRole.USER).ratings(4.1f).lastLogin("20분 전").build();
		
		userRepo.save(u1);
		userRepo.save(u2);
		userRepo.save(u3);
		userRepo.save(u4);
		userRepo.save(u5);
		
		userRepo.save(t1);
		userRepo.save(t2);
		userRepo.save(t3);
		userRepo.save(t4);
		userRepo.save(t5);
		
		Post p1 = Post.builder().userId(6L).writer("닉네임1").type(PostType.MATE).title("제목입니다 111").content("내용입니다 111").country("한국").city("강릉").viewCount(123L).likeCount(18L).rating(81F).status(PostStatus.OPEN).build();
		Post p2 = Post.builder().userId(7L).writer("닉네임2").type(PostType.MATE).title("제목입니다 222").content("내용입니다 222").country("베트남").city("하노이").viewCount(234L).likeCount(28L).rating(23F).status(PostStatus.OPEN).build();
		Post p3 = Post.builder().userId(8L).writer("닉네임3").type(PostType.MATE).title("제목입니다 333").content("내용입니다 333").country("태국").city("방콕").viewCount(456L).likeCount(38L).rating(45F).status(PostStatus.CLOSED).build();
		Post p4 = Post.builder().userId(9L).writer("닉네임4").type(PostType.MATE).title("제목입니다 444").content("내용입니다 444").country("영국").city("런던").viewCount(345L).likeCount(78L).rating(88F).status(PostStatus.CLOSED).build();
		Post p5 = Post.builder().userId(10L).writer("닉네임5").type(PostType.MATE).title("제목입니다 555").content("내용입니다 555").country("프랑스").city("파리").viewCount(567L).likeCount(128L).rating(96F).status(PostStatus.OPEN).build();
		Post p6 = Post.builder().userId(6L).writer("닉네임1").type(PostType.MATE).title("제목입니다 666").content("내용입니다 666").country("스페인").city("바르셀로나").viewCount(789L).likeCount(98L).rating(72F).status(PostStatus.CLOSED).build();
		
		Post p7 = Post.builder().userId(6L).writer("닉네임1").type(PostType.COURSE).title("course 1").content("내용입니다 666").country("스페인").city("바르셀로나").viewCount(789L).likeCount(98L).rating(72F).status(PostStatus.CLOSED).build();
		Post p8 = Post.builder().userId(7L).writer("닉네임2").type(PostType.COURSE).title("course 2").content("내용입니다 666").country("스페인").city("바르셀로나").viewCount(789L).likeCount(98L).rating(72F).status(PostStatus.CLOSED).build();
		Post p9 = Post.builder().userId(8L).writer("닉네임3").type(PostType.COURSE).title("course 3").content("내용입니다 666").country("스페인").city("바르셀로나").viewCount(789L).likeCount(98L).rating(72F).status(PostStatus.CLOSED).build();
		Post p10 = Post.builder().userId(9L).writer("닉네임4").type(PostType.COURSE).title("course 4").content("내용입니다 666").country("스페인").city("바르셀로나").viewCount(789L).likeCount(98L).rating(72F).status(PostStatus.CLOSED).build();
		Post p11 = Post.builder().userId(10L).writer("닉네임5").type(PostType.COURSE).title("course 5").content("내용입니다 666").country("스페인").city("바르셀로나").viewCount(789L).likeCount(98L).rating(72F).status(PostStatus.CLOSED).build();
		
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
		
        System.out.println("\n\n### ### ### ### ### ### ### ###");
        System.out.println("#                             #");
		System.out.println("#   서버가 성공적으로 실행되었습니다.   #");
		System.out.println("#                             #");
		System.out.println("### ### ### ### ### ### ### ###");
	}

}
