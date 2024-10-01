package com.example.tripDuo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.dto.ChatMessageDto;
import com.example.tripDuo.entity.ChatMessage;
import com.example.tripDuo.entity.ChatRoom;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
	List<ChatMessage> findByChatRoomId(Long roomId);
	List<ChatMessage> save(Long roomId);


}
