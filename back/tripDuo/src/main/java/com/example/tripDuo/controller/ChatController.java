package com.example.tripDuo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tripDuo.dto.ChatMessageDto;
import com.example.tripDuo.dto.ChatRoomDto;
import com.example.tripDuo.entity.ChatRoom;
import com.example.tripDuo.service.ChatService;

@RestController
public class ChatController {

	private final ChatService chatService;

	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}

	// 현재 사용자가 속한 채팅방 목록을 반환하는 API
	@GetMapping("/api/chat/rooms")
	public ResponseEntity<List<ChatRoomDto>> getAllChatRooms(@RequestParam Long userId) {
		
		return ResponseEntity.ok(chatService.getAllChatRooms(userId));
	}

	// 특정 채팅방의 모든 메시지를 반환하는 API
	@GetMapping("/api/chat/rooms/{roomId}/messages")
	public ResponseEntity<List<ChatMessageDto>> getChatMessages(@PathVariable Long roomId) {
		
		return ResponseEntity.ok(chatService.getChatMessages(roomId));
	}

	// 채팅방 생성 API
	@PostMapping("/api/chat/rooms")
	public ResponseEntity<ChatRoom> createChatRoom(@RequestBody ChatRoomDto chatRoomDto) {

		return ResponseEntity.ok(chatService.createChatRoom(chatRoomDto));
	}

}
