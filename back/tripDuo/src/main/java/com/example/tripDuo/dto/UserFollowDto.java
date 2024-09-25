package com.example.tripDuo.dto;

import java.time.LocalDateTime;

import com.example.tripDuo.entity.UserFollow;
import com.example.tripDuo.enums.FollowType;

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
	
	private Long followeeUserId; // 팔로우 당하는 사람 (팔로이)
	private Long followerUserId; // 팔로우 하는 사람 (팔로워)
	
	private FollowType followType; // 팔로우, 차단 
	
	private LocalDateTime createdAt;
	
	public static UserFollowDto toDto(UserFollow entity) {
		return UserFollowDto.builder()
    			.id(entity.getId())
    			.followeeUserId(entity.getFolloweeUserProfileInfo().getUser().getId())
    			.followerUserId(entity.getFollowerUserProfileInfo().getUser().getId())
    			.followType(entity.getFollowType())
    			.createdAt(entity.getCreatedAt())
				.build();
	}
}
