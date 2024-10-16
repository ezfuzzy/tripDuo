package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.enums.AccountStatus;
import com.example.tripDuo.enums.UserRole;
import com.example.tripDuo.enums.VerificationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@Table(name="users", indexes = {
		@Index(name = "idx_users_username", columnList = "username"),
		@Index(name = "idx_users_encryted_phone_number", columnList = "encryptedPhoneNumber")
}) 
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 20)
    private String username;
    
    @Column(nullable = false, length = 60)
    private String password;
    
    @Column(nullable = false, length = 30, unique = true)
    private String encryptedPhoneNumber;
    
    @Column(nullable = false, length = 50, unique = true)
    private String email; // [note: "인증 받으면 email 로그인 사용 가능 ?? "]
    
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private VerificationStatus verificationStatus; // 인증 상태 enum [note: "unverified, verified, pending"]
    
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private AccountStatus accountStatus; // 관리자의 조치 enum [note: "active, inactive, suspended"]
              
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private UserRole role; // enum [note: "user, manager, admin"]

    private LocalDateTime createdAt; 
    private LocalDateTime updatedAt; 
    private LocalDateTime deletedAt; //  [note:"soft delete 지원?"]
    
    @PrePersist
    public void onPrePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
    	updatedAt = LocalDateTime.now();
    }

    public void softDelete() { 
    	accountStatus = AccountStatus.INACTIVE;
    	deletedAt = LocalDateTime.now();
    }

    public void updateAccountStatus(AccountStatus newAccountStatus) {
        accountStatus = newAccountStatus;
    }
    
    public static User toEntity(UserDto dto) {
                
        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .encryptedPhoneNumber(dto.getEncryptedPhoneNumber())
                .email(dto.getEmail())
                .verificationStatus(dto.getVerificationStatus())
                .accountStatus(dto.getAccountStatus())
                .role(dto.getRole())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .deletedAt(dto.getDeletedAt())
                .build();    
    }
}