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
    private String writer;
    private String profilePicture;
    
    private String content;

    private Long parentCommentId;
    private String toUsername;

    private CommentStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    // ### for app ###
    
    private int startRowNum;
    private int endRowNum;
    private int pageNum;
    
    
    
    public static PostCommentDto toDto(PostComment entity) {
        return PostCommentDto.builder()
            .id(entity.getId())
            .postId(entity.getPostId())
            .userId(entity.getUserProfileInfo() != null ? entity.getUserProfileInfo().getId() : null)
            .writer(entity.getUserProfileInfo() != null ? entity.getUserProfileInfo().getNickname() : null)
            .profilePicture(entity.getUserProfileInfo() != null ? entity.getUserProfileInfo().getProfilePicture() : null)
            .content(entity.getContent())
            .parentCommentId(entity.getParentCommentId())
            .toUsername(entity.getToUsername())
            .status(entity.getStatus())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .deletedAt(entity.getDeletedAt())
            .build();
    }
}
