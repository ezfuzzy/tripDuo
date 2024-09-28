package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.UserReviewDto;
import com.example.tripDuo.enums.ReviewExperience;
import com.example.tripDuo.enums.ReviewTag;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	
	@ManyToOne
	@JoinColumn(name = "reviewer_id", nullable = false) // 리뷰 하는 사람 (리뷰어)
	private UserProfileInfo reviewerUserProfileInfo;
	
	private String content;
	
	@Enumerated(EnumType.STRING)
	private ReviewExperience experience;
	
	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass = ReviewTag.class)
	private ReviewTag[] tags;
	
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

	public long getRating() {
		long rating = 0;
		for (ReviewTag tag : tags) {
			switch (tag) {
			case COMMUNICATION:
			case TRUST:
			case ONTIME:
			case MANNER:
				rating += 40;
				break;

			case FLEXIBLE:
			case ACTIVE:
			case FRIENDLY:
			case PAY:
			case CLEAN:
				rating += 20;
				break;
			}
		};

		switch (experience) {
			case BAD:
				rating *= -2;
				break;
			case GOOD:
				rating *= 1;
				break;
			case EXCELLENT:
				rating *= 1.5;
				break;
		}

		return rating;
	}
    
    public void updateContent(String newContent) {
    	content = newContent;
    }
    
    public static UserReview toEntity(UserReviewDto dto, UserProfileInfo reviewerUpi) {
    	return UserReview.builder()
    			.id(dto.getId())
    			.revieweeId(dto.getRevieweeId() != null ? dto.getRevieweeId() : 0L)
    			.reviewerUserProfileInfo(reviewerUpi)
    			.content(dto.getContent())
    			.experience(dto.getExperience())
    			.tags(dto.getTags())
    			.createdAt(dto.getCreatedAt())
    			.updatedAt(dto.getUpdatedAt())
    			.build();
    }
}