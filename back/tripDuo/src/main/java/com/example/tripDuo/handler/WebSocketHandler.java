package com.example.tripDuo.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.tripDuo.service.ChatService;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
	private ChatService chatService;

	public WebSocketHandler(ChatService chatService) {
		this.chatService = chatService;
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
	    System.out.println("Received message: " + message.getPayload()); // 로그 추가
	    
//		// 1. 클라이언트로부터 들어온 메시지 수신
//		String payload = message.getPayload();
//		// 2. 메시지를 ChatMessage 객체로 변환
//		ChatMessage chatMessage = new ObjectMapper().readValue(payload, ChatMessage.class);
//		// 3. 메시지를 처리하기 위해 서비스 클래스에 전달
//		chatService.saveMessageToRedis(chatMessage);
//		// 4. 클라이언트에 응답 메시지 전송 (옵션)
//		session.sendMessage(new TextMessage("Message cached in Redis"));
	}

}
