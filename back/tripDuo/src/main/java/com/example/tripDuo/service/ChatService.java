package com.example.tripDuo.service;

import java.util.List;

import com.example.tripDuo.dto.ChatMessageDto;
import com.example.tripDuo.dto.ChatRoomDto;
import com.example.tripDuo.entity.ChatRoom;

public interface ChatService {
	public List<ChatRoomDto> getAllChatRooms(Long userId);
    // 특정 채팅방의 모든 메시지를 반환
	public List<ChatMessageDto> getChatMessages(Long roomId);
    // 추가: 채팅방 생성 메서드
	public ChatRoom createChatRoom(ChatRoomDto chatRoomDto); // 채팅방 생성 추가
}
