package com.example.tripDuo.dto;

import com.example.tripDuo.entity.ReportToUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class ReportToUserDto extends ReportDto {
    private Long reportedUserId;

    public static ReportToUserDto toDto(ReportToUser entity) {
        return ReportToUserDto.builder()
                .id(entity.getId())
                .reporterId(entity.getReporterId())
                .content(entity.getContent())
                .reportStatus(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .reportedUserId(entity.getReportedUser() != null ? entity.getReportedUser().getId() : null)
                .build();
    }
}