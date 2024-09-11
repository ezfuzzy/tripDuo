package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.PostDto;
import com.example.tripDuo.enums.PostStatus;
import com.example.tripDuo.enums.PostType;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name="posts") // 인덱스 추가 
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;
    private String writer;
    
    @Enumerated(EnumType.STRING)
    private PostType type;

    private String title;    
    private String content; // 메이트, 커뮤니티 게시글에만 있음
    
//    @Convert(converter = JsonNodeConverter.class)  // converter
    @Column(length = 10000)
    private JsonNode postData; // 코스, 여행기 게시글에만 있음
    
    private String country;
    private String city;

    @Column(columnDefinition = "TEXT[]")
    private String[] tags;

    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private Float rating;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @PrePersist
    public void onPrePersist() {
        createdAt = LocalDateTime.now();
    }
    
    public void setLikeCount(Long likeCount) {
    	this.likeCount = likeCount;
    }

    public void setCommentCount(Long commentCount) {
    	this.commentCount = commentCount;
    }
    
    public void updateRating(Float rating) {
        this.rating = rating;
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
                .commentCount(dto.getCommentCount())
                .rating(dto.getRating())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .deletedAt(dto.getDeletedAt())
                .build();
    }
}
