package com.example.tripDuo.entity;

import com.example.tripDuo.dto.PostRatingDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name="post_ratings")
public class PostRating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long postId;
    private Long userId;

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal rating;

    private LocalDateTime createdAt;

    public static PostRating toEntity(PostRatingDto dto) {
        return PostRating.builder()
            .id(dto.getId())
            .postId(dto.getPostId())
            .userId(dto.getUserId())
            .rating(dto.getRating())
            .createdAt(dto.getCreatedAt())
            .build();
    }
}
