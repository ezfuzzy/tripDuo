package com.example.tripDuo.entity;


import com.example.tripDuo.dto.PostCommentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public enum Status {
        ON,        // 댓글 활성화 상태
        SECRET,    // 비밀댓글
        DELETED    // 삭제됨
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
            .status(PostComment.Status.valueOf(dto.getStatus().name()))  // Status 변환
            .createdAt(dto.getCreatedAt())
            .updatedAt(dto.getUpdatedAt())
            .deletedAt(dto.getDeletedAt())
            .build();
    }
}
