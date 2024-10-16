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
// target_type 컬럼에 POST_COMMENT 를 저장
@DiscriminatorValue("POST_COMMENT")
public class ReportToPostComment extends Report {

    @ManyToOne
    @JoinColumn(name = "reported_post_comment_id")
    private PostComment reportedPostComment;

    public static ReportToPostComment toEntity(ReportDto dto, PostComment postComment) {
        return ReportToPostComment.builder()
                .id(dto.getId())
                .reporterId(dto.getReporterId() != null ? dto.getReporterId() : 0L)
                .reportedUserUserId(dto.getReportedUserId() != null ? dto.getReportedUserId() : 0L)
                .content(dto.getContent())
                .status(dto.getReportStatus())
                .createdAt(dto.getCreatedAt())
                .reportedPostComment(postComment)
                .build();
    }
}