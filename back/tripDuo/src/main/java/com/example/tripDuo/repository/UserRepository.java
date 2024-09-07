package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.User;



public interface UserRepository extends JpaRepository<User, Long> {
	public User findByUsername(String username);
	public User findByPhoneNumber(String phoneNumber);
	public User findByEmail(String email);

	// username 중복 여부 체크
	public Boolean existsByUsername(String username);
	public Boolean existsByNickname(String nickname);
	public Boolean existsByPhoneNumber(String phoneNumber);
	public Boolean existsByEmail(String email);
}