package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
	User findByPhoneNumber(String phoneNumber);
	User findByEmail(String email);

	// username 중복 여부 체크
	boolean existsByUsername(String username);
	boolean existsByNickname(String nickname);
	boolean existsByPhoneNumber(String phoneNumber);
	boolean existsByEmail(String email);
}