package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.UserDto;

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

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name="users") // 인덱스 추가 
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String username;
    private String password;
    
    private String nickname;
              
    private Long age;
    private String gender;
    private String phoneNumber;
    private String email; // [note: "인증 받으면 email 로그인 사용 가능 ?? "]

    private String profilePicture;
    private String profileMessage;
    
    private String curLocation;
    private String verificationStatus; // 인증 상태 enum [note: "unverified, verified, pending"]
    private String accountStatus; // 관리자의 조치 enum [note: "active, inactive, suspended"]
    private String socialLinks;
              
    private String role; // enum [note: "user, manager, admin"]

    private Float ratings; // 지표 설정 

    private String lastLogin; // 몇분전 접속  
    
    private LocalDateTime createdAt; 
    private LocalDateTime updatedAt; 
    private LocalDateTime deletedAt; //  [note:"soft delete 지원?"]
    
    @PrePersist
    public void onPrePersist() {
        createdAt = LocalDateTime.now();
    }

    public static User toEntity(UserDto dto) {
                
        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .age(dto.getAge())
                .gender(dto.getGender())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .profilePicture(dto.getProfilePicture())
                .profileMessage(dto.getProfileMessage())
                .curLocation(dto.getCurLocation())
                .verificationStatus(dto.getVerificationStatus())
                .accountStatus(dto.getAccountStatus())
                .socialLinks(dto.getSocialLinks())
                .role(dto.getRole())
                .ratings(dto.getRatings())
                .lastLogin(dto.getLastLogin())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .deletedAt(dto.getDeletedAt())
                .build();    
    }
}