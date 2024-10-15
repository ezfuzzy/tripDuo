package com.example.tripDuo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.tripDuo.entity.ChatRoom;
import com.example.tripDuo.enums.ChatRoomType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder 
public class ChatRoomDto {
	private Long id;
	private ChatRoomType type;
	private String title;
	
	// ### for app ###
	
	private Long ownerId;
	private List<Long> participantsList;
	private LocalDateTime lastmessagetime;

	public static ChatRoomDto toDto(ChatRoom entity) {
		return ChatRoomDto.builder()
				.id(entity.getId())
				.type(entity.getType())
				.title(entity.getTitle())
				.lastmessagetime(entity.getLastmessagetime())
				.build();
	}
}