package com.example.tripDuo.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
		 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		Date createdAt = null;
	    Date updatedAt = null;
	    Date deletedAt = null;

	    try {
	        if (dto.getCreated_at() != null) {
	            createdAt = formatter.parse(dto.getCreated_at());
	        }
	        if (dto.getUpdated_at() != null) {
	            updatedAt = formatter.parse(dto.getUpdated_at());
	        }
	        if (dto.getDeleted_at() != null) {
	            deletedAt = formatter.parse(dto.getDeleted_at());
	        }
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    
		 return User.builder()
		            .id(dto.getId())
		            .username(dto.getUsername())
		            .password(dto.getPassword())
		            .nickname(dto.getNickname())
		            .age(dto.getAge())
		            .name(dto.getName())
		            .gender(dto.getGender())
		            .phone_num(dto.getPhone_num())
		            .email(dto.getEmail())
		            .profile_pics(dto.getProfile_pics())
		            .profile_msg(dto.getProfile_msg())
		            .cur_location(dto.getCur_location())
		            .verification_status(dto.getVerification_status())
		            .account_status(dto.getAccount_status())
		            .social_links(dto.getSocial_links())
		            .role(dto.getRole())
		            .ratings(dto.getRatings())
		            .last_login(dto.getLast_login())
		            .created_at(createdAt)
		            .updated_at(updatedAt)
		            .deleted_at(deletedAt)
		            .build();	
	 }
}
