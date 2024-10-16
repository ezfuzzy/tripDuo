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
//target_type 컬럼에 USER_REVIEW 를 저장
@DiscriminatorValue("USER_REVIEW")
public class ReportToUserReview extends Report {

    @ManyToOne
    @JoinColumn(name = "reported_user_review_id")
    private UserReview reportedUserReview;

    public static ReportToUserReview toEntity(ReportDto dto, UserReview userReview) {
        return ReportToUserReview.builder()
                .id(dto.getId())
                .reporterId(dto.getReporterId() != null ? dto.getReporterId() : 0L)
                .reportedUserUserId(dto.getReportedUserId() != null ? dto.getReportedUserId() : 0L)
                .content(dto.getContent())
                .status(dto.getReportStatus())
                .createdAt(dto.getCreatedAt())
                .reportedUserReview(userReview)
                .build();
    }
}