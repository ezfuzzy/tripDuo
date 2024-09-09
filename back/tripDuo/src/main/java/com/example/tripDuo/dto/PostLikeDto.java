package com.example.tripDuo.dto;

import java.time.LocalDateTime;

import com.example.tripDuo.entity.PostLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostLikeDto {
    private Long id;
    
    private Long postId;
    private Long userId;
    
    private LocalDateTime createdAt;

    public static PostLikeDto toEntity(PostLike entity){
        return PostLikeDto.builder()
            .id(entity.getId())
            .postId(entity.getPostId())
            .userId(entity.getUserId())
            .createdAt(entity.getCreatedAt())
            .build();
    }
}
