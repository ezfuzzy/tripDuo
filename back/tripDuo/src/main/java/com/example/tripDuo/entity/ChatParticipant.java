package com.example.tripDuo.entity;

import com.example.tripDuo.dto.ChatMessageDto;
import com.example.tripDuo.dto.ChatParticipantDto;

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
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 포함한 생성자
@Table(name = "chat_participants") // 테이블 이름 설정
public class ChatParticipant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long chatRoomId;
	

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserProfileInfo userProfileInfo;

	private boolean isOwner; // 방장 여부를 구분하는 필드

	// toEntity 메서드
		public static ChatParticipant toEntity(ChatParticipantDto dto, UserProfileInfo userProfileInfo) {
		        return ChatParticipant.builder()
		                .id(dto.getId())
		                .chatRoomId(dto.getChatRoomId())
		                .userProfileInfo(userProfileInfo)
		                .isOwner(dto.isOwner())
		                .build();

		}
}
