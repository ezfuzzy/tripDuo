package com.example.tripDuo.dto;

import java.time.LocalDateTime;

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
    
    private float rating;
    
    private LocalDateTime createdAt; 
}
