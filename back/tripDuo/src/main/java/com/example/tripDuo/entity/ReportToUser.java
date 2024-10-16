package com.example.tripDuo.entity;

import com.example.tripDuo.dto.ReportDto;

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
public class ReportToUser extends Report {
    
    @ManyToOne
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    public static ReportToUser toEntity(ReportDto dto, User user) {
        return ReportToUser.builder()
                .id(dto.getId())
                .reporterId(dto.getReporterId() != null ? dto.getReporterId() : 0L)
                .reportedUserUserId(dto.getReportedUserId() != null ? dto.getReportedUserId() : 0L)
                .content(dto.getContent())
                .status(dto.getReportStatus())
                .createdAt(dto.getCreatedAt())
                .reportedUser(user)
                .build();
    }
}