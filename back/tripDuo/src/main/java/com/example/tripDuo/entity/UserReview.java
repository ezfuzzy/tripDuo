package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.UserReviewDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@Table(name="user_reviews")
public class UserReview {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private long revieweeId; // 리뷰 당하는 사람 (리뷰이)
	private long reviewerId; // 리뷰 하는 사람 (리뷰어) << 얘만 ManyToOne
	
	private String content;
	
	@Column(columnDefinition = "TEXT[]")
	private String[] tags;
	
	private float rating;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
    @PrePersist
    public void onPrePersist() {
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    public void onPreUpdate() {
    	updatedAt = LocalDateTime.now();
    }
    
    public static UserReview toEntity(UserReviewDto dto) {
    	return UserReview.builder()
    			.id(dto.getId())
    			.reviewerId(dto.getReviewerId() != null ? dto.getReviewerId() : 0L)
    			.revieweeId(dto.getRevieweeId() != null ? dto.getRevieweeId() : 0L)
    			.content(dto.getContent())
    			.tags(dto.getTags())
    			.rating(dto.getRating() != null ? dto.getRating() : 0F)
    			.createdAt(dto.getCreatedAt())
    			.updatedAt(dto.getUpdatedAt())
    			.build();
    }
}