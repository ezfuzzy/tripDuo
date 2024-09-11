package com.example.tripDuo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.tripDuo.entity.PostRating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostRatingDto {
    private Long id;
    
    private Long postId;
    private Long userId;
    
    private BigDecimal rating;
    
    private LocalDateTime createdAt;

    public static PostRatingDto toEntity(PostRating entity) {
        return PostRatingDto.builder()
            .id(entity.getId())
            .postId(entity.getPost() != null ? entity.getPost().getId() : null)
            .userId(entity.getUserId())
            .rating(entity.getRating())
            .createdAt(entity.getCreatedAt())
            .build();
    }
}
