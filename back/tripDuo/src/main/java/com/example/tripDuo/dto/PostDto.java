package com.example.tripDuo.dto;

import java.time.LocalDateTime;

import com.example.tripDuo.entity.Post;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostDto {
    private Long id;
    
    private Long userId;
    private String writer;
    private String type; // mate / course / 여행기 / 커뮤니티
    private String title;
    
    private String content; // 메이트, 커뮤니티 게시글에만 있음
    private JsonNode postData; // 코스, 여행기 게시글에만 있음
    
    private String country;
    private String city;
    
    private String tags;
    
    private Long viewCount;
    private Long likeCount;
    private Float rating;
    private String status; // mate 모집(구인)중, 모집완료, 삭제됨 등
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    
    
    public static PostDto toDto(Post entity) {
        return PostDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .type(entity.getType())
                .title(entity.getTitle())
                .content(entity.getContent())
                .postData(entity.getPostData())
                .country(entity.getCountry())
                .city(entity.getCity())
                .tags(entity.getTags())
                .viewCount(entity.getViewCount())
                .likeCount(entity.getLikeCount())
                .rating(entity.getRating())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt() : LocalDateTime.now()) // createdAt은 null일 경우 현재 시간으로 설정
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
    
}