package com.example.tripDuo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.ChatParticipant;

public interface ChatParticipantsRepository extends JpaRepository<ChatParticipant, Long> {
	// 사용자가 속한 채팅방 목록을 가져오는 메서드
    List<ChatParticipant> findByUserProfileInfoUserId(Long userId);
}
