package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.UserProfileInfo;

public interface UserProfileInfoRepository extends JpaRepository<UserProfileInfo, Long>{
	UserProfileInfo findByUserId(Long userId);
	
	boolean existsByNickname(String nickname);
}
