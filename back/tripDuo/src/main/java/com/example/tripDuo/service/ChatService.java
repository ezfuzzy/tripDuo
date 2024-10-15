package com.example.tripDuo.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.socket.TextMessage;

import com.example.tripDuo.dto.ChatMessageDto;
import com.example.tripDuo.dto.ChatRoomDto;
import com.example.tripDuo.entity.ChatRoom;
import com.example.tripDuo.entity.UserFollow;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ChatService  {
	// 맞팔 리스트 조회 
	public List<UserFollow> getSelectUser(Long userId);
	// 사용자가 속한 채팅방 번호 조회
	public List<ChatRoomDto> getSelectAllChatRooms(Long userId);
	// 선택된 채팅방 정보를 조회 
	public ChatRoomDto getSelectUserChatRoom(Long roomId);
	// 특정 채팅방의 모든 메시지를 반환
	public Map<String, Object> getChatMessages(Long roomId);
	// 채팅방 생성 추가
	public ChatRoom createChatRoom(ChatRoomDto chatRoomDto);
	// 메세지 캐싱
	public ChatMessageDto saveMessageToRedis(TextMessage message) throws JsonProcessingException;
	// 메세지 DB 저장 스케줄러
	public void saveMessagesToDatabase();
	public void clearRedisCacheOnShutdown();
	public ChatMessageDto saveMessageToDatabsePassive(ChatMessageDto chatMessageDto);
	
}
