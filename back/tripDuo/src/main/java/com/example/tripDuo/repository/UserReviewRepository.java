package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.UserReview;

public interface UserReviewRepository extends JpaRepository<UserReview, Long>{
	long countByReviewerId(Long followerUserId); // 해당 유저가 review 하는 사람이 몇명인지
	long countByRevieweeId(Long followeeUserId); // 해당 유저를 review 하는 사람이 몇명인지
}
