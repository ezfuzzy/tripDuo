package com.example.tripDuo.dto;

import com.example.tripDuo.entity.ReportToPostComment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class ReportToPostCommentDto extends ReportDto {
    private Long reportedPostCommentId;

    public static ReportToPostCommentDto toDto(ReportToPostComment entity) {
        return ReportToPostCommentDto.builder()
                .id(entity.getId())
                .reporterId(entity.getReporterId())
                .content(entity.getContent())
                .reportStatus(entity.getStatus())
                .createdAt(entity.getCreatedAt())   
                .reportedPostCommentId(entity.getReportedPostComment() != null ? entity.getReportedPostComment().getId() : null)
                .build();
    }
}
