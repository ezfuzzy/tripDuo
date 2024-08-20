package com.example.tripDuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.tripDuo.entity.User;
import com.example.tripDuo.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;

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
		User u1 = User.builder().username("ezfz").password(encoder.encode("hysz")).username("fuzzy").gender("남자").role("admin").build(); 
		User u2 = User.builder().username("aaaa").password(encoder.encode("aaaa")).username("userA").gender("남자").role("user").build();
		User u3 = User.builder().username("bbbb").password(encoder.encode("bbbb")).username("userB").gender("여자").role("user").build();
		User u4 = User.builder().username("cccc").password(encoder.encode("cccc")).username("userC").gender("여자").role("user").build();
		User u5 = User.builder().username("dddd").password(encoder.encode("dddd")).username("userD").gender("남자").role("user").build();
		
		userRepo.save(u1);
		userRepo.save(u2);
		userRepo.save(u3);
		userRepo.save(u4);
		userRepo.save(u5);
	}

}
