package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.UserProfileInfoDto;
import com.example.tripDuo.enums.Gender;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name="user_profile_infos", indexes = {
		@Index(name = "idx_user_profile_infos_user_id", columnList = "user_id")
}) 
public class UserProfileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false, length = 20)
    private String nickname;
    
    private Long age;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 6)
    private Gender gender;

    private String profilePicture;

    @Column(length = 100)
    private String profileMessage;

    @Column(length = 50)
    private String curLocation;
    
    @Column(columnDefinition = "TEXT[]")
    private String[] socialLinks;
    
    // 0 ~ 10000점
    @Builder.Default
    private Long ratings = (long)1300; // 점수 설정 

    private LocalDateTime lastLogin; // 몇분전 접속  

    public void addRatings(long addedRating) {
        if (ratings + addedRating > 10000) {
            ratings = 10000L;
        } else if (ratings + addedRating < 0) {
            ratings = 0L;
        } else {
            ratings += addedRating;
        }
    }

    public static UserProfileInfo toEntity(UserProfileInfoDto dto, User user) {
    	
    	
    	return UserProfileInfo.builder()
    			.id(dto.getId())
    			.user(user)
    			.nickname(dto.getNickname())
    			.age(dto.getAge())
    			.gender(dto.getGender())
    			.profilePicture(dto.getProfilePicture())
    			.profileMessage(dto.getProfileMessage())
    			.curLocation(dto.getCurLocation())
    			.socialLinks(dto.getSocialLinks())
    			.ratings(dto.getRatings() != null ? dto.getRatings() : 0L)
    			.build();
    }
}
