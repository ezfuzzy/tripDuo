package com.example.tripDuo.entity;

import java.util.Date;

import com.example.tripDuo.dto.UserDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name = "USERS")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String username;
	private String password;
	
	private String nickname;
			  
	private int age;
	private String name;
	private String gender;
	private String phone_num;
	private String email; // [note: "인증 받으면 email 로그인 사용 가능"]

	private String profile_pics;
	private String profile_msg;
	
	private String cur_location;
	private String verification_status; // [note: "인증 상태"]
	private String account_status; // [note: "관리자의 조치"]
	private String social_links;
			  
	private String role; // [note: "user / manager / admin"]

	private float ratings; // 지표 설정 

	private String last_login; // 몇분전 접속  
	
	private Date created_at; 
	private Date updated_at; 
	private Date deleted_at; //  [note:"soft delete 지원?"]
	
	@PrePersist
	public void onPrepersist() {
		created_at = new Date();
	}

	
	public static User toEntity(UserDto dto) {
		return User.builder().build();
	}
}
