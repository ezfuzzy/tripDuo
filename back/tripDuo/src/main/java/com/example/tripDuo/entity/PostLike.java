package com.example.tripDuo.entity;

import com.example.tripDuo.dto.PostLikeDto;
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
@Table(name="post_likes")
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long postId;
    private Long userId;

    private LocalDateTime createdAt;

    public static PostLike toEntity(PostLikeDto dto){
        return PostLike.builder()
            .id(dto.getId())
            .postId(dto.getPostId())
            .userId(dto.getUserId())
            .createdAt(dto.getCreatedAt())
            .build();
    }
}
