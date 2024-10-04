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
// target_type 컬럼에 POST 를 저장
@DiscriminatorValue("POST")
public class ReportToPost extends Report {

    @ManyToOne
    @JoinColumn(name = "reported_post_id")
    private Post reportedPost;

    public static ReportToPost toEntity(ReportDto dto, Post post) {
        return ReportToPost.builder()
                .id(dto.getId())
                .reporterId(dto.getReporterId() != null ? dto.getReporterId() : 0L)
                .content(dto.getContent())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .reportedPost(post)
                .build();
    }
}