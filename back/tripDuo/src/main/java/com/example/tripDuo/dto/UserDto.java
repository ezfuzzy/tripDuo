package com.example.tripDuo.dto;

import com.example.tripDuo.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {
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
	
	private String created_at; 
	private String updated_at; 
	private String deleted_at; //  [note:"soft delete 지원?"]
	
	public static UserDto toDto(User entity) {
		return UserDto.builder().build();
	}
}
