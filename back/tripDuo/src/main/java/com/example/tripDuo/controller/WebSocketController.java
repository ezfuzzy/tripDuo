package com.example.tripDuo.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.example.tripDuo.dto.ChatMessageDto;
import com.example.tripDuo.dto.ChatRoomDto;
import com.example.tripDuo.dto.NotificationDto;

@RestController
public class WebSocketController {

	private final SimpMessagingTemplate messagingTemplate;

	public WebSocketController(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

//    // 1. 그룹 채팅 메시지 전송
//    @MessageMapping("/chat.sendMessage/group")
//    @SendTo("/topic/{roomId}")  // 그룹 채팅방
//    public ChatMessageDto sendGroupMessage(ChatMessageDto messageDto, @DestinationVariable String roomId) {
//        return messageDto;
//    }
	// 1. 그룹 채팅 메시지 전송
	@MessageMapping("/chat.sendMessage/group/{roomId}")
	@SendTo("/topic/group/{roomId}") // 그룹 채팅방
	public ChatMessageDto sendGroupMessage2(ChatMessageDto messageDto, @DestinationVariable String roomId) {
		return messageDto;
	}

	// 2. 1:1 채팅 메시지 전송
	@MessageMapping("/chat.sendMessage/private/{roomId}")
	@SendTo("/user/private/{roomId}") // 1:1 채팅방
	public ChatMessageDto sendPrivateMessage(ChatMessageDto messageDto, @DestinationVariable String roomId) {
		return messageDto;
	}

	// 3. 알림 전송
	@MessageMapping("/notification")
	@SendTo("/notification")
	public NotificationDto sendNotification(NotificationDto notification) {
		return notification;
	}

	// 4. 사용자가 채팅방에 참여할 때
	@MessageMapping("/chat.addUser")
	@SendTo("/topic/adduser/{roomId}")
	public ChatMessageDto addUser(ChatMessageDto message, SimpMessageHeaderAccessor headerAccessor,
			@DestinationVariable String roomId) {
		// WebSocket 세션에 사용자 추가
		headerAccessor.getSessionAttributes().put("username", message.getSender());
		return message;
	}

	// 5. 사용자가 채팅방에 참여할 때
	@MessageMapping("/chat.addchatroom/{roomId}")
	@SendTo("/topic/newroom/{roomId}")
	public ChatRoomDto addChatRoom(ChatRoomDto message, SimpMessageHeaderAccessor headerAccessor,
			@DestinationVariable String roomId) {
		// WebSocket 세션에 사용자 추가
		return message;
	}
}