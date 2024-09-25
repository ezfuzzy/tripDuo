package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.UserTripInfo;

public interface UserTripInfoRepository extends JpaRepository<UserTripInfo, Long> {
	UserTripInfo findByUserId(Long userId);
}
