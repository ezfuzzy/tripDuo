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
	
	private Long revieweeId; // 리뷰 당하는 사람 (리뷰이)
	
	private Long reviewerId; // 리뷰 하는 사람 (리뷰어)
	private String reviewerProfilePicture;
    private String reviewerNickname;
	
	private String content;
	
	private String[] tags;
	
	private Float rating;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public static UserReviewDto toDto(UserReview entity, String cloudFrontUrl) {
    	String reviewerProfilePictureUrl = entity.getReviewerUserProfileInfo().getProfilePicture();
    	if(reviewerProfilePictureUrl != null && !reviewerProfilePictureUrl.isEmpty() && cloudFrontUrl != null && !cloudFrontUrl.isEmpty()) {
    		reviewerProfilePictureUrl = cloudFrontUrl + reviewerProfilePictureUrl;
    	}
		
		return UserReviewDto.builder()
				.id(entity.getId())
				.revieweeId(entity.getRevieweeId())
				.reviewerId(entity.getReviewerUserProfileInfo().getUser().getId())
				.reviewerProfilePicture(reviewerProfilePictureUrl)
				.reviewerNickname(entity.getReviewerUserProfileInfo().getNickname())
				.content(entity.getContent())
				.tags(entity.getTags())
				.rating(entity.getRating())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();
	}
}