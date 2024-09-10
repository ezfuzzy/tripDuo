package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.UserFollowDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name="user_follows")
public class UserFollow {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String followerUserId; // 팔로우 하는 사람 (팔로워)
	private String followeeUserId; // 팔로우 당하는 사람 (팔로이)
	
	private String status;
	
	private LocalDateTime createdAt;
	
    @PrePersist
    public void onPrePersist() {
        createdAt = LocalDateTime.now();
    }
	
    public static UserFollow toEntity(UserFollowDto dto) {
    	return UserFollow.builder()
    			.id(dto.getId())
    			.followerUserId(dto.getFollowerUserId())
    			.followeeUserId(dto.getFolloweeUserId())
    			.status(dto.getStatus())
    			.createdAt(dto.getCreatedAt())
    			.build();
    }
    
}
