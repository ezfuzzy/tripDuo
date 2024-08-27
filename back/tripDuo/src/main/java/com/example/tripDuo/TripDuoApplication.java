package com.example.tripDuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.tripDuo.entity.User;
import com.example.tripDuo.repository.UserRepository;

import jakarta.annotation.PostConstruct;

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
	
	@PostConstruct
	public void init() {
		System.out.println("\n### init ###");
		User u1 = User.builder().username("ezfz").password(encoder.encode("hysz")).gender("남자").role("admin").build(); 
		User u2 = User.builder().username("aaaa").password(encoder.encode("aaaa")).gender("남자").role("user").build();
		User u3 = User.builder().username("bbbb").password(encoder.encode("bbbb")).gender("여자").role("user").build();
		User u4 = User.builder().username("cccc").password(encoder.encode("cccc")).gender("여자").role("user").build();
		User u5 = User.builder().username("dddd").password(encoder.encode("dddd")).gender("남자").role("user").build();
		
		User t1 = User.builder().id(1).username("user1").password(encoder.encode("password1")).nickname("닉네임1").age(25).name("홍길동").gender("남자").phone_num("010-1234-5678").email("user1@example.com").profile_pics(null).profile_msg("안녕하세요!").cur_location("서울").verification_status("인증됨").account_status("정상").social_links("http://social1.com").role("user").ratings(4.5f).last_login("5분 전").build();
		User t2 = User.builder().id(2).username("user2").password(encoder.encode("password2")).nickname("닉네임2").age(30).name("김철수").gender("남자").phone_num("010-2345-6789").email("user2@example.com").profile_pics(null).profile_msg("안녕하세요!").cur_location("부산").verification_status("인증됨").account_status("정상").social_links("http://social2.com").role("user").ratings(3.8f).last_login("10분 전").build();
		User t3 = User.builder().id(3).username("user3").password(encoder.encode("password3")).nickname("닉네임3").age(22).name("이영희").gender("여자").phone_num("010-3456-7890").email("user3@example.com").profile_pics(null).profile_msg("안녕하세요!").cur_location("대구").verification_status("인증됨").account_status("정상").social_links("http://social3.com").role("user").ratings(4.2f).last_login("15분 전").build();
		User t4 = User.builder().id(4).username("user4").password(encoder.encode("password4")).nickname("닉네임4").age(28).name("박민수").gender("남자").phone_num("010-4567-8901").email("user4@example.com").profile_pics(null).profile_msg("안녕하세요!").cur_location("인천").verification_status("인증됨").account_status("정상").social_links("http://social4.com").role("user").ratings(4.0f).last_login("20분 전").build();
		User t5 = User.builder().id(5).username("user5").password(encoder.encode("password5")).nickname("닉네임5").age(35).name("최지훈").gender("남자").phone_num("010-5678-9012").email("user5@example.com").profile_pics(null).profile_msg("안녕하세요!").cur_location("광주").verification_status("인증됨").account_status("정상").social_links("http://social5.com").role("user").ratings(4.1f).last_login("20분 전").build();
		
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
	}

}
