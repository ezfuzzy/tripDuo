package com.example.tripDuo.dto;

import com.example.tripDuo.entity.ChatMessage;
import com.example.tripDuo.entity.ChatRoom;
import com.example.tripDuo.enums.ChatType;

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
	private ChatType type;
	private String name;
	private String content;
	private String sender;

public static ChatRoomDto toDto(ChatRoom entity, ChatMessage lastMessage) {
    return ChatRoomDto.builder()
    		 .id(entity.getId())
	         .type(entity.getType())
	         .name(entity.getName())
	         .content(lastMessage != null ? lastMessage.getContent() : "No messages yet")
	         .sender(lastMessage != null ? lastMessage.getSender() : "No sender")
	         .build();
	}
}