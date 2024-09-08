package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.PostDto;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "posts")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;
    private String writer;
    private String type; // mate / course / 여행기 / 커뮤니티

    private String title;    
    private String content; // 메이트, 커뮤니티 게시글에만 있음
    
//    @Convert(converter = JsonNodeConverter.class)  // converter
    @Column(length = 10000)
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

    @PrePersist
    public void onPrePersist() {
        createdAt = LocalDateTime.now();
    }
    
    public static Post toEntity(PostDto dto) {
    	
        return Post.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .writer(dto.getWriter())
                .type(dto.getType())
                .title(dto.getTitle())
                .content(dto.getContent())
                .postData(dto.getPostData())
                .country(dto.getCountry())
                .city(dto.getCity())
                .tags(dto.getTags())
                .viewCount(dto.getViewCount())
                .likeCount(dto.getLikeCount())
                .rating(dto.getRating())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now()) // createdAt은 null일 경우 현재 시간으로 설정
                .updatedAt(dto.getUpdatedAt())
                .deletedAt(dto.getDeletedAt())
                .build();
    }
}
