package com.example.tripDuo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.tripDuo.dto.PostRatingDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="post_ratings")
public class PostRating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    private long userId;

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal rating;

    private LocalDateTime createdAt;

    @PrePersist
    public void onPrePersist() {
        createdAt = LocalDateTime.now();
    }
    
    public static PostRating toEntity(PostRatingDto dto, Post post) {
        return PostRating.builder()
            .id(dto.getId())
            .post(post)
            .userId(dto.getUserId() != null ? dto.getUserId() : 0L)
            .rating(dto.getRating())
            .createdAt(dto.getCreatedAt())
            .build();
    }
}
