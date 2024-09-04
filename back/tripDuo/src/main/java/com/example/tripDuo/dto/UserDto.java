package com.example.tripDuo.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.example.tripDuo.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {
    private Long id;
    
    private String username;
    private String password;
    private String newPassword;
    private String newConfirmPassword;
    
    private String nickname;
              
    private Long age;
    private String name;
    private String gender;
    private String phoneNumber;
    private String email; // [note: "인증 받으면 email 로그인 사용 가능"]

    private String profilePicture;
    private String profileMessage;
    
    private String curLocation;
    private String verificationStatus; // [note: "인증 상태"]
    private String accountStatus; // [note: "관리자의 조치"]
    private String socialLinks;
              
    private String role; // [note: "user / manager / admin"]

    private Float ratings; // 지표 설정 

    private String lastLogin; // 몇분전 접속  
    
    private LocalDateTime createdAt; 
    private LocalDateTime updatedAt; 
    private LocalDateTime deletedAt; //  [note:"soft delete 지원?"]
    
    @JsonIgnore
    private MultipartFile profileImgForUpload;
    
    public static UserDto toDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .nickname(entity.getNickname())
                .age(entity.getAge())
                .name(entity.getName())
                .gender(entity.getGender())
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .profilePicture(entity.getProfilePicture())
                .profileMessage(entity.getProfileMessage())
                .curLocation(entity.getCurLocation())
                .verificationStatus(entity.getVerificationStatus())
                .accountStatus(entity.getAccountStatus())
                .socialLinks(entity.getSocialLinks())
                .role(entity.getRole())
                .ratings(entity.getRatings())
                .lastLogin(entity.getLastLogin())
                .createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt() : null)
                .updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt() : null)
                .deletedAt(entity.getDeletedAt() != null ? entity.getDeletedAt() : null)
                .build();
    }
}