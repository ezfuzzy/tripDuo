package com.example.tripDuo.dto;

import com.example.tripDuo.entity.ChatParticipant;
import com.example.tripDuo.entity.UserProfileInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatParticipantDto {
	private Long id;
	private Long chatRoomId;
	private Long userId;
	private boolean isOwner; // 방장 여부를 구분하는 필드
	
	public static ChatParticipantDto toDto(ChatParticipant entity, UserProfileInfo userProfileInfo) {
	    return ChatParticipantDto.builder()
	    		 .id(entity.getId())
	    		 .chatRoomId(entity.getChatRoomId())
	    		 .userId(userProfileInfo.getUser().getId())
	    		 .isOwner(entity.isOwner())
		         .build();
		}
}
