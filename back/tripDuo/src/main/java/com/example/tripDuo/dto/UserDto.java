package com.example.tripDuo.dto;

import java.time.LocalDateTime;

import com.example.tripDuo.entity.User;
import com.example.tripDuo.enums.AccountStatus;
import com.example.tripDuo.enums.UserRole;
import com.example.tripDuo.enums.VerificationStatus;

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
    private String confirmPassword;
    
    private String nickname;
    
    private String phoneNumber;
    private String email; // [note: "인증 받으면 email 로그인 사용 가능"]

    private VerificationStatus verificationStatus; // [note: "인증 상태"]
    private AccountStatus accountStatus; // [note: "관리자의 조치"]
              
    private UserRole role; // [note: "user / manager / admin"]

    private LocalDateTime createdAt; 
    private LocalDateTime updatedAt; 
    private LocalDateTime deletedAt; //  [note:"soft delete 지원?"]
        
    public static UserDto toDto(User entity) {
    	
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .verificationStatus(entity.getVerificationStatus())
                .accountStatus(entity.getAccountStatus())
                .role(entity.getRole())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}