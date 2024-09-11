package com.example.tripDuo.dto;

import java.time.LocalDateTime;

import com.example.tripDuo.entity.UserReview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserReviewDto {
	private Long id;
	
	private Long reviewerId; // 리뷰 하는 사람 (리뷰어)
	private Long revieweeId; // 리뷰 당하는 사람 (리뷰이)
	
	private String content;
	
	private String[] tags;
	
	private Float rating;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public static UserReviewDto toDto(UserReview entity) {
		return UserReviewDto.builder()
				.id(entity.getId())
				.reviewerId(entity.getReviewerId())
				.revieweeId(entity.getRevieweeId())
				.content(entity.getContent())
				.tags(entity.getTags())
				.rating(entity.getRating())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();
	}
}