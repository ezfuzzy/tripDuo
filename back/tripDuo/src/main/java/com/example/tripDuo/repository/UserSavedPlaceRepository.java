package com.example.tripDuo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.UserSavedPlace;

public interface UserSavedPlaceRepository extends JpaRepository<UserSavedPlace, Long>{
	List<UserSavedPlace> findByUserId(Long userId);
}
