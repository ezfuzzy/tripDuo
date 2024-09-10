package com.example.tripDuo.dto;

import java.time.LocalDateTime;

import com.example.tripDuo.entity.UserFollow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserFollowDto {
	
	private Long id;
	
	private String followerUserId; // 팔로우 하는 사람 (팔로워)
	private String followeeUserId; // 팔로우 당하는 사람 (팔로이)
	
	private String status; // 팔로우, 차단 
	
	private LocalDateTime createdAt;
	
	public static UserFollowDto toDto(UserFollow entity) {
		return UserFollowDto.builder()
    			.id(entity.getId())
    			.followerUserId(entity.getFollowerUserId())
    			.followeeUserId(entity.getFolloweeUserId())
    			.status(entity.getStatus())
    			.createdAt(entity.getCreatedAt())
				.build();
	}
}
