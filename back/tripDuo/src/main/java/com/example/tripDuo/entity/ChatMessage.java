package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.ChatMessageDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor  // 기본 생성자
@AllArgsConstructor // 모든 필드를 포함한 생성자
@Table(name="chatmessage") // 인덱스 추가 
public class ChatMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String content;
	private String sender;

	@ManyToOne
	@JoinColumn(name = "chat_room_id")
	private ChatRoom chatRoom;

	private LocalDateTime timestamp;

	// toEntity 메서드
	public static ChatMessage toEntity(ChatMessageDto chatMessageDto, ChatRoom chatRoom) {
	        return ChatMessage.builder()
	                .id(chatMessageDto.getId())
	                .content(chatMessageDto.getContent())
	                .sender(chatMessageDto.getSender())
	                .timestamp(chatMessageDto.getTimestamp())
	                .chatRoom(chatRoom)
	                .build();

	}
}
