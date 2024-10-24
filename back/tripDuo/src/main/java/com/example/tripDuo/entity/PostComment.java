package com.example.tripDuo.entity;


import java.time.LocalDateTime;

import com.example.tripDuo.dto.PostCommentDto;
import com.example.tripDuo.enums.CommentStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name="post_comments", indexes = {
		@Index(name = "idx_post_comments_post_id", columnList = "postId")
})
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private long postId;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfileInfo userProfileInfo;

    @Column(nullable = false, length = 100)
    private String content;

    private long parentCommentId;

    @Column(length = 20)
    private String toUsername;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private CommentStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    
    @PrePersist
    public void onPrePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
    	updatedAt = LocalDateTime.now();
    }
    
    public void setParentCommentId(long parentCommentId) {
    	this.parentCommentId = parentCommentId;
    }
    
    public void softDeletePostComment() {
    	deletedAt = LocalDateTime.now();
    	status = CommentStatus.DELETED;
    }
    
    public static PostComment toEntity(PostCommentDto dto, UserProfileInfo userProfileInfo) {
        return PostComment.builder()
            .id(dto.getId())
            .postId(dto.getPostId() != null ? dto.getPostId() : 0L)
            .userProfileInfo(userProfileInfo)
            .content(dto.getContent())
            .parentCommentId(dto.getParentCommentId() != null ? dto.getParentCommentId() : 0L)
            .toUsername(dto.getToUsername())
            .status(dto.getStatus())
            .createdAt(dto.getCreatedAt())
            .updatedAt(dto.getUpdatedAt())
            .deletedAt(dto.getDeletedAt())
            .build();
    }
}
