package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.User;



public interface UserRepository extends JpaRepository<User, Long> {
	public User findByUsername(String username);
	public User findByPhoneNumber(String phoneNumber);
	public User findByEmail(String email);
	
	public void save(Long kakaoId);
}