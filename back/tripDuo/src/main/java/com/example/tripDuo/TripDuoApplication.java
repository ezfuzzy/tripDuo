package com.example.tripDuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.tripDuo.entity.User;
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
	private PasswordEncoder encoder;
	
	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		System.out.println("\n### init ###");
		User u1 = User.builder().username("ezfz").password(encoder.encode("hysz")).gender("남자").role("admin").build(); 
		User u2 = User.builder().username("aaaa").password(encoder.encode("aaaa")).gender("남자").role("user").build();
		User u3 = User.builder().username("bbbb").password(encoder.encode("bbbb")).gender("여자").role("user").build();
		User u4 = User.builder().username("cccc").password(encoder.encode("cccc")).gender("여자").role("user").build();
		User u5 = User.builder().username("dddd").password(encoder.encode("dddd")).gender("남자").role("user").build();
		
		User t1 = User.builder().username("user1").password(encoder.encode("password1")).nickname("닉네임1").age((long) 25).name("홍길동").gender("남자").phoneNumber("010-1234-5678").email("user1@example.com").profileMessage("안녕하세요1").curLocation("서울").verificationStatus("인증됨").accountStatus("정상").socialLinks("http://social1.com").role("user").ratings(4.5f).lastLogin("5분 전").build();
		User t2 = User.builder().username("user2").password(encoder.encode("password2")).nickname("닉네임2").age((long) 30).name("김철수").gender("남자").phoneNumber("010-2345-6789").email("user2@example.com").profileMessage("안녕하세요2").curLocation("부산").verificationStatus("인증됨").accountStatus("정상").socialLinks("http://social2.com").role("user").ratings(3.8f).lastLogin("10분 전").build();
		User t3 = User.builder().username("user3").password(encoder.encode("password3")).nickname("닉네임3").age((long) 22).name("이영희").gender("여자").phoneNumber("010-3456-7890").email("user3@example.com").profileMessage("안녕하세요3").curLocation("대구").verificationStatus("인증됨").accountStatus("정상").socialLinks("http://social3.com").role("user").ratings(4.2f).lastLogin("15분 전").build();
		User t4 = User.builder().username("user4").password(encoder.encode("password4")).nickname("닉네임4").age((long) 28).name("박민수").gender("남자").phoneNumber("010-4567-8901").email("user4@example.com").profileMessage("안녕하세요4").curLocation("인천").verificationStatus("인증됨").accountStatus("정상").socialLinks("http://social4.com").role("user").ratings(4.0f).lastLogin("20분 전").build();
		User t5 = User.builder().username("user5").password(encoder.encode("password5")).nickname("닉네임5").age((long) 35).name("최지훈").gender("남자").phoneNumber("010-5678-9012").email("user5@example.com").profileMessage("안녕하세요5").curLocation("광주").verificationStatus("인증됨").accountStatus("정상").socialLinks("http://social5.com").role("user").ratings(4.1f).lastLogin("20분 전").build();
		
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
		System.out.println("\n\n 서버가 성공적으로 실행되었습니다.");
	}

}
