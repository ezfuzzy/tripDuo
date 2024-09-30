package com.example.tripDuo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.tripDuo.dto.ChatMessageDto;
import com.example.tripDuo.dto.ChatRoomDto;
import com.example.tripDuo.entity.ChatMessage;
import com.example.tripDuo.entity.ChatRoom;

public interface ChatService {
    // 사용자가 속한 채팅방 번호 조회
	public List<ChatRoomDto> getSelectAllChatRooms(Long userId);
    // 특정 채팅방의 모든 메시지를 반환
	public List<ChatMessageDto> getChatMessages(Long roomId);
	// 채팅방 생성 추가
	public ChatRoom createChatRoom(ChatRoomDto chatRoomDto); 
	// 메세지 전송 및 저장
	public List<ChatMessageDto> saveMesages(Long roomId, ChatMessageDto chatMessageDto);
}
