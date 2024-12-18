package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.UserFollowDto;
import com.example.tripDuo.enums.FollowType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name="user_follows", indexes = {
		@Index(name = "idx_user_follows_user_user", columnList = "followee_user_id, follower_user_id")
})
public class UserFollow {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "followee_user_id", nullable = false)
	private UserProfileInfo followeeUserProfileInfo;
	
	@ManyToOne
    @JoinColumn(name = "follower_user_id", nullable = false)
    private UserProfileInfo followerUserProfileInfo;
	
    @Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private FollowType followType;
	
	private LocalDateTime createdAt;
	
    @PrePersist
    public void onPrePersist() {
        createdAt = LocalDateTime.now();
    }
	
    public void updateFollowType(FollowType followType) {
    	this.followType = followType;
    }
    
    @PreUpdate
    public void updateCreatedAt() {
    	createdAt = LocalDateTime.now();
    }
    
    public static UserFollow toEntity(UserFollowDto dto, UserProfileInfo followeeUpi, UserProfileInfo followerUpi) {
    	return UserFollow.builder()
    			.id(dto.getId())
    			.followeeUserProfileInfo(followeeUpi)
    			.followerUserProfileInfo(followerUpi)
    			.followType(dto.getFollowType())
    			.createdAt(dto.getCreatedAt())
    			.build();
    }
    
}
