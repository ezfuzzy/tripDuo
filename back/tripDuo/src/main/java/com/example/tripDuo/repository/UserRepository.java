package com.example.tripDuo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public User findByUsername(String username);
	
	public Optional<User> findById(Long id);
	
	public void save(Long kakaoId);
}