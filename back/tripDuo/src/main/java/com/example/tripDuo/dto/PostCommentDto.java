package com.example.tripDuo.dto;

import java.time.LocalDateTime;

import com.example.tripDuo.entity.PostComment;
import com.example.tripDuo.enums.CommentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostCommentDto {

    private Long id;

    private Long postId;
    private Long userId;

    private String content;

    private Long groupId;
    private Long depth;
    private Long toUserId;

    private CommentStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static PostCommentDto toDto(PostComment entity) {
        return PostCommentDto.builder()
            .id(entity.getId())
            .postId(entity.getPostId())
            .userId(entity.getUserId())
            .content(entity.getContent())
            .groupId(entity.getGroupId())
            .depth(entity.getDepth())
            .toUserId(entity.getToUserId())
            .status(entity.getStatus())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .deletedAt(entity.getDeletedAt())
            .build();
    }
}
