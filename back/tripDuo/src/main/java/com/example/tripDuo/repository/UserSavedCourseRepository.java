package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.UserSavedCourse;

public interface UserSavedCourseRepository extends JpaRepository<UserSavedCourse, Long>{
	void deleteByCourse_IdAndUserId(Long postId, Long userId);
}
