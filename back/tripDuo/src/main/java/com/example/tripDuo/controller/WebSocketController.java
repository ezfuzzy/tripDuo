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
		System.out.println(notification);
		if (notification.getType() == "CHATROOM") {
			// 서비스 가서 디비 저장하고
			// 저장된 chatroom 반환 받아서
			// notification에 넣어서 리턴
		}
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

	// 5. 그룹 채팅방 생성
	@MessageMapping("/chat.addChatroom/group/{roomId}")
	@SendTo("/topic/newroom/group/{roomId}")
	public ChatRoomDto addChatRoomGroup(ChatRoomDto message, SimpMessageHeaderAccessor headerAccessor,
			@DestinationVariable String roomId) {
		// WebSocket 세션에 사용자 추가
		return message;
	}

	// 6. 1:1 채팅방 생성
	@MessageMapping("/chat.addChatroom/private/{roomId}")
	@SendTo("/user/newroom/private/{roomId}")
	public ChatRoomDto addChatRoomPrivate(ChatRoomDto message, SimpMessageHeaderAccessor headerAccessor,
			@DestinationVariable String roomId) {
		// WebSocket 세션에 사용자 추가
		return message;
	}
}