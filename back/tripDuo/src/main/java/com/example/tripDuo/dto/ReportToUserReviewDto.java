package com.example.tripDuo.dto;

import com.example.tripDuo.entity.ReportToUserReview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class ReportToUserReviewDto extends ReportDto {
    private Long reportedUserReviewId;

    public static ReportToUserReviewDto toDto(ReportToUserReview entity) {
        return ReportToUserReviewDto.builder()
                .id(entity.getId())
                .reporterId(entity.getReporterId())
                .content(entity.getContent())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .reportedUserReviewId(entity.getReportedUserReview() != null ? entity.getReportedUserReview().getId() : null)
                .build();
    }
}