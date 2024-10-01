package com.example.tripDuo.dto;

import com.example.tripDuo.entity.UserReportToUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class UserReportToUserDto extends UserReportDto {
    private Long reportedUserId;

    public static UserReportToUserDto toDto(UserReportToUser entity) {
        return UserReportToUserDto.builder()
                .id(entity.getId())
                .reporterId(entity.getReporterId())
                .content(entity.getContent())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .reportedUserId(entity.getReportedUser() != null ? entity.getReportedUser().getId() : null)
                .build();
    }
}