package com.example.tripDuo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.UserVisitedPlace;

public interface UserVisitedPlaceRepository extends JpaRepository<UserVisitedPlace, Long> {
	List<UserVisitedPlace> findByUserId(Long userId);
}
