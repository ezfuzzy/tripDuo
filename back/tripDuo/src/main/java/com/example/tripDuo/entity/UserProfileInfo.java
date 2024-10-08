package com.example.tripDuo.entity;

import com.example.tripDuo.dto.UserProfileInfoDto;
import com.example.tripDuo.enums.Gender;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name="user_profile_infos") // 인덱스 추가 
public class UserProfileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String nickname;
    
    private Long age;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    private String profilePicture;
    private String profileMessage;
    
    private String curLocation;
    
    private String socialLinks; // > json 처리
    
    private float ratings; // 지표 설정 

    private String lastLogin; // 몇분전 접속  
    
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
    			.ratings(dto.getRatings() != null ? dto.getRatings() : 0.0f)
    			.build();
    }
}
