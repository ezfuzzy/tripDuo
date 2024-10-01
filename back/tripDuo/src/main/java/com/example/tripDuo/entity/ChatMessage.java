package com.example.tripDuo.entity;

import java.time.LocalDateTime;
import java.util.Date;

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
@Table(name="chat_message") // 인덱스 추가 
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private Long chatRoomId;  // 필드 이름 수정

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfileInfo userProfileInfo;
    
    private Date timestamp;

    // toEntity 메서드
    public static ChatMessage toEntity(ChatMessageDto dto, UserProfileInfo userProfileInfo) {
        return ChatMessage.builder()
        		.id(dto.getId())
                .message(dto.getMessage())
                .userProfileInfo(userProfileInfo)
                .chatRoomId(dto.getChatRoomId())  // 필드 이름 수정
                .timestamp(dto.getTimestamp())
                .build();
    }
}
