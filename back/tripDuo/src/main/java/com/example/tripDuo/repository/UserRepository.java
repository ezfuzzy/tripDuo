package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
	User findByEncryptedPhoneNumber(String encryptedPhoneNumber);
	User findByEmail(String email);

	// username 중복 여부 체크
	boolean existsByUsername(String username);
	boolean existsByEncryptedPhoneNumber(String phoneNumber);
	boolean existsByEmail(String email);
}