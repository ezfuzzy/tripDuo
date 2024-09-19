package com.example.tripDuo.dto;

import java.time.LocalDateTime;

import com.example.tripDuo.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
	private Long id;
	private String content;
	private String sender;
	private Long chatRoomId;
	private LocalDateTime timestamp;

public static ChatMessageDto toDto(ChatMessage entity) {
    return ChatMessageDto.builder()
    		.id(entity.getId())
            .content(entity.getContent())
            .sender(entity.getSender())
            .chatRoomId(entity.getChatRoom().getId()) // ChatRoom 엔티티의 ID만 가져옴
            .timestamp(entity.getTimestamp())
            .build();
	}
}
