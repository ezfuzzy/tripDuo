package com.example.tripDuo.dto;

import com.example.tripDuo.entity.PostComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public enum Status {
        ON,        // 댓글 활성화 상태
        SECRET,    // 비밀댓글
        DELETED    // 삭제됨
    }

    public static PostCommentDto toDto(PostComment entity) {
        return PostCommentDto.builder()
            .id(entity.getId())
            .postId(entity.getPostId())
            .userId(entity.getUserId())
            .content(entity.getContent())
            .groupId(entity.getGroupId())
            .depth(entity.getDepth())
            .toUserId(entity.getToUserId())
            .status(PostCommentDto.Status.valueOf(entity.getStatus().name()))  // Status 변환
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .deletedAt(entity.getDeletedAt())
            .build();
    }
}
