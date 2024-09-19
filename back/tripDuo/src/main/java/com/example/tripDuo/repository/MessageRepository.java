package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.ChatMessage;
import com.example.tripDuo.entity.ChatRoom;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {

	ChatMessage findTopByChatRoomOrderByTimestampDesc(ChatRoom room);

}
