package com.example.tripDuo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.tripDuo.dto.ChatMessageDto;
import com.example.tripDuo.dto.ChatRoomDto;
import com.example.tripDuo.entity.ChatMessage;
import com.example.tripDuo.enums.ChatType;
import com.example.tripDuo.service.ChatService;

@Controller
public class ChatController {

	private final  ChatService chatService;

	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}
	// WebSocket을 통해 메시지 전송 (클라이언트에서 /app/chat.sendMessage로 전송됨)
	@MessageMapping("/chat.sendMessage/{roomId}")
	@SendTo("/topic/room/{roomId}")
	public List<ChatMessageDto> sendMessage(@DestinationVariable Long roomId, ChatMessageDto chatMessageDto) {
	    // 메시지 저장 또는 로깅 로직
        chatService.sendMessage(chatMessageDto);
	    return chatService.getChatMessages(roomId);  
	}

	@MessageMapping("/chat.addUser/{roomId}")
	@SendTo("/topic/room/{roomId}")
	public ChatRoomDto addUser(ChatRoomDto chatDto) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		chatDto.setContent(userName + "님이 입장하셨습니다.");
		return chatDto;
		}

    // 모든 채팅방 목록을 반환하는 API
	@GetMapping("/api/chat/rooms")
	public ResponseEntity<List<ChatRoomDto>> getAllChatRooms() {
		List<ChatRoomDto> rooms = chatService.getAllChatRooms();
		return ResponseEntity.ok(rooms);
	}
    // 특정 채팅방의 모든 메시지를 반환하는 API
	@GetMapping("/api/chat/rooms/{roomId}/messages")
	public ResponseEntity<List<ChatMessageDto>> getChatMessages(@PathVariable Long roomId) {
		return ResponseEntity.ok(chatService.getChatMessages(roomId));
	}

	 // POST로 메시지 전송을 처리하는 엔드포인트 추가
    @PostMapping("/sendMessage")
    public ResponseEntity<List<ChatMessageDto>> sendMessage(@RequestBody ChatMessageDto chatMessageDto) {
//        chatService.sendMessage(chatMessageDto);
        return ResponseEntity.ok().build();
    }
    
    // 채팅방 생성 API
    @PostMapping("/api/chat/rooms")
    public ResponseEntity<ChatRoomDto> createChatRoom(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        ChatRoomDto newRoom = chatService.createChatRoom(name, ChatType.ONE_ON_ONE, null);  // 채팅방 생성
        return ResponseEntity.ok(newRoom);  // 생성된 채팅방 반환
    }
}
