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
	private String message;
	
	private String sender;
	private String profilePicture;
	
	private Long chatRoomId;
    private String recipient;  // 1:1 채팅에서 사용할 수신자 필드
    
	private LocalDateTime timestamp;

public static ChatMessageDto toDto(ChatMessage entity) {
    return ChatMessageDto.builder()
    		.id(entity.getId())
            .message(entity.getMessage())
            .sender(entity.getUserProfileInfo().getNickname())
            .profilePicture(entity.getUserProfileInfo().getProfilePicture())
            .timestamp(entity.getTimestamp())
            .build();
	}
}
