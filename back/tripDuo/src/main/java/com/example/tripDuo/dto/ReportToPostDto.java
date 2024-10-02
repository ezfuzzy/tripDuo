package com.example.tripDuo.dto;

import com.example.tripDuo.entity.ReportToPost;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class ReportToPostDto extends ReportDto {
    private Long reportedPostId;

    public static ReportToPostDto toDto(ReportToPost entity) {
        return ReportToPostDto.builder()
                .id(entity.getId())
                .reporterId(entity.getReporterId())
                .content(entity.getContent())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .reportedPostId(entity.getReportedPost() != null ? entity.getReportedPost().getId() : null)
                .build();
    }
}
