package com.example.tripDuo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.UserReview;

public interface UserReviewRepository extends JpaRepository<UserReview, Long>{
	// get 할 때 리뷰들을 가져오는 메소드
	List<UserReview> findByRevieweeId(Long revieweeId, Sort sort);
	
	// update 할 때 기존 리뷰가 있는지 찾는 메소드
	UserReview findByRevieweeIdAndReviewerUserProfileInfo_User_Id(Long revieweeId, Long reviewerId);
	
	// 리뷰를 delete하는 메소드
	void deleteByRevieweeIdAndReviewerUserProfileInfo_User_Id(Long revieweeId, Long reviewerId);
}