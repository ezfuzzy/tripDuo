package com.example.tripDuo.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.tripDuo.dto.UserDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name = "USERS")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String username;
    private String password;
    
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
    
    private Date createdAt; 
    private Date updatedAt; 
    private Date deletedAt; //  [note:"soft delete 지원?"]
    
    @PrePersist
    public void onPrePersist() {
        createdAt = new Date();
    }

    public static User toEntity(UserDto dto) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         
        Date createdAt = null;
        Date updatedAt = null;
        Date deletedAt = null;

        try {
            if (dto.getCreatedAt() != null) {
                createdAt = formatter.parse(dto.getCreatedAt());
            }
            if (dto.getUpdatedAt() != null) {
                updatedAt = formatter.parse(dto.getUpdatedAt());
            }
            if (dto.getDeletedAt() != null) {
                deletedAt = formatter.parse(dto.getDeletedAt());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .age(dto.getAge())
                .name(dto.getName())
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
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();    
    }
}