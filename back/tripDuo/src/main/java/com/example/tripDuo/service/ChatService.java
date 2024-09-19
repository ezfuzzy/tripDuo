package com.example.tripDuo.service;

import java.util.List;

import com.example.tripDuo.dto.ChatMessageDto;
import com.example.tripDuo.dto.ChatRoomDto;
import com.example.tripDuo.entity.ChatMessage;
import com.example.tripDuo.enums.ChatType;

public interface ChatService {
	public List<ChatRoomDto> getAllChatRooms();
	public ChatRoomDto getChatRoom(Long id,  ChatMessage lastMessage);
	public ChatMessageDto sendMessage(ChatMessageDto chatMessageDto);
	// 추가: 채팅방의 모든 메시지를 반환하는 메서드
    List<ChatMessageDto> getChatMessages(Long roomId);
    // 추가: 채팅방 생성 메서드
    ChatRoomDto createChatRoom(String name, ChatType type, ChatMessage lastMessage); // 채팅방 생성 추가
}
