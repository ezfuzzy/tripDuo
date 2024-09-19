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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfileInfo userProfileInfo;
    
    @Enumerated(EnumType.STRING)
    private PostType type;

    @Column(nullable = false)
    private String title;    
    
    @Column(nullable = false)
    private String content; // 메이트, 커뮤니티 게시글에만 있음
    
//    @Convert(converter = JsonNodeConverter.class)  // converter
    @Column(length = 10000)
    private JsonNode postData; // 코스, 여행기 게시글에만 있음
    
    private String country;
    private String city;

    @Column(columnDefinition = "TEXT[]")
    private String[] tags;

    private long viewCount;
    private long likeCount;
    private long commentCount;
    private float rating;

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
        status = PostStatus.DELETED;
    }
    
    public void softDeletePostComment() {
    	deletedAt = LocalDateTime.now();
    }
    
    public static Post toEntity(PostDto dto, UserProfileInfo userProfileInfo) {
        
        return Post.builder()
                .id(dto.getId())
                .userProfileInfo(userProfileInfo)
                .type(dto.getType())
                .title(dto.getTitle())
                .content(dto.getContent())
                .postData(dto.getPostData())
                .country(dto.getCountry())
                .city(dto.getCity())
                .tags(dto.getTags())
                .viewCount(dto.getViewCount() != null ? dto.getViewCount() : 0L)
                .likeCount(dto.getLikeCount() != null ? dto.getLikeCount() : 0L)
                .commentCount(dto.getCommentCount() != null ? dto.getCommentCount() : 0L)
                .rating(dto.getRating() != null ? dto.getRating() : 0.0f)
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .deletedAt(dto.getDeletedAt())
                .build();
    }
}
