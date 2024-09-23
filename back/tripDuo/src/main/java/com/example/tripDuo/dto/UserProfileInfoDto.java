package com.example.tripDuo.dto;

import org.springframework.web.multipart.MultipartFile;

import com.example.tripDuo.entity.UserProfileInfo;
import com.example.tripDuo.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserProfileInfoDto {

	private Long id;
    
	private Long userId;
	
    private String nickname;
    
    private Long age;
    
    private Gender gender;
    
    private String profilePicture;
    private String profileMessage;
    
    private String curLocation;
    
    private String socialLinks; // > json 처리
    
    private Float ratings; // 지표 설정 

    private String lastLogin; // 몇분전 접속
    
    // ### for app ###
    
    @JsonIgnore
    private MultipartFile profileImgForUpload;
    
    private Long follwerCount;
    private Long follweeCount;
    
    private String token;
    
    // UserProfileInfoRepository에서 follower/followee의 정보를 사용
    public UserProfileInfoDto(Long userId, String nickname, String profilePicture, String profileMessage) {
        this.userId = userId;
        this.nickname = nickname;
        this.profilePicture = profilePicture;
        this.profileMessage = profileMessage;
    }
    
    public static UserProfileInfoDto toDto(UserProfileInfo entity, String cloudFrontUrl) {
    	
    	String profilePictureUrl = entity.getProfilePicture();
    	if(profilePictureUrl != null && !profilePictureUrl.isEmpty() && cloudFrontUrl != null && !cloudFrontUrl.isEmpty()) {
    		profilePictureUrl = cloudFrontUrl + profilePictureUrl;
    	}
    	
        return UserProfileInfoDto.builder()
                .id(entity.getId())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .nickname(entity.getNickname())
                .age(entity.getAge())
                .gender(entity.getGender())
                .profilePicture(profilePictureUrl)
                .profileMessage(entity.getProfileMessage())
                .curLocation(entity.getCurLocation())
                .socialLinks(entity.getSocialLinks())
                .ratings(entity.getRatings())
                .lastLogin(entity.getLastLogin())
                .build();
    }
}
