package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
import jakarta.persistence.Index;
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
@Table(name="posts", indexes = {
		@Index(name = "idx_posts_type", columnList = "type"),
		@Index(name = "idx_posts_user_id", columnList = "user_id"),
		@Index(name = "idx_posts_city", columnList = "city")
})
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfileInfo userProfileInfo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PostType type;

    @Column(nullable = false, length = 50)
    private String title;    

    @Column(length = 3000)
    private String content; // 메이트, 커뮤니티 게시글에만 있음
    
    @JdbcTypeCode(SqlTypes.JSON) // jsonb 타입을 명시
    @Column(columnDefinition = "jsonb")  // DB에 jsonb 타입으로 지정
    private JsonNode postData; // 코스, 여행기 게시글에만 있음

    @Column(nullable = false, length = 30)
    private String country;
    @Column(length = 30)
    private String city;

    @Column(length = 40)
    private String startDate;

    @Column(length = 40)
    private String endDate;
    
    @Column(columnDefinition = "TEXT[]")
    private String[] tags;

    private long viewCount;
    private long likeCount;
    private long commentCount;
    private float rating;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
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
    
    public void softDeletePost() {
    	deletedAt = LocalDateTime.now();
    	status = PostStatus.DELETED;
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
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
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
