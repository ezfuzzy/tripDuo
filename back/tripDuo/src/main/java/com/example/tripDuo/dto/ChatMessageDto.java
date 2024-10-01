package com.example.tripDuo.dto;

import java.util.Date;

import com.example.tripDuo.entity.ChatMessage;
import com.example.tripDuo.entity.UserProfileInfo;

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

	private UserProfileInfo userProfileInfo;
	
	private Long chatRoomId;

	private Date timestamp;

	public static ChatMessageDto toDto(ChatMessage entity) {
		return ChatMessageDto.builder()
				.id(entity.getId())
				.message(entity.getMessage())
				.userProfileInfo(entity.getUserProfileInfo())
				.chatRoomId(entity.getChatRoomId())
				.timestamp(entity.getTimestamp())
				.build();
	}
}
