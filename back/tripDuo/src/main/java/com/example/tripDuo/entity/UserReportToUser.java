package com.example.tripDuo.entity;

import com.example.tripDuo.dto.UserReportDto;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Entity
// target_type 컬럼에 USER 를 저장
@DiscriminatorValue("USER")
public class UserReportToUser extends UserReport {
    
    @ManyToOne
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    public static UserReportToUser toEntity(UserReportDto dto, User user) {
        return UserReportToUser.builder()
                .id(dto.getId())
                .reporterId(dto.getReporterId() != null ? dto.getReporterId() : 0L)
                .content(dto.getContent())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .reportedUser(user)
                .build();
    }
}