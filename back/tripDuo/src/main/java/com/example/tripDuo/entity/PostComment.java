package com.example.tripDuo.entity;


import java.time.LocalDateTime;

import com.example.tripDuo.dto.PostCommentDto;
import com.example.tripDuo.enums.CommentStatus;

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
@Table(name="post_comments")
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long postId;
    private Long userId;

    private String content;
    private Long groupId;
    private Long depth;
    private Long toUserId;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    
    @PrePersist
    public void onPrePersist() {
        createdAt = LocalDateTime.now();
    }

    public static PostComment toEntity(PostCommentDto dto) {
        return PostComment.builder()
            .id(dto.getId())
            .postId(dto.getPostId())
            .userId(dto.getUserId())
            .content(dto.getContent())
            .groupId(dto.getGroupId())
            .depth(dto.getDepth())
            .toUserId(dto.getToUserId())
            .status(dto.getStatus())
            .createdAt(dto.getCreatedAt())
            .updatedAt(dto.getUpdatedAt())
            .deletedAt(dto.getDeletedAt())
            .build();
    }
}
