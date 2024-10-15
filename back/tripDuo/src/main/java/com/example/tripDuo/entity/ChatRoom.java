package com.example.tripDuo.entity;


import java.time.LocalDateTime;
import java.util.Date;

import com.example.tripDuo.dto.ChatRoomDto;
import com.example.tripDuo.enums.ChatRoomType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name="chat_room") // 테이블 이름 설정
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  

    private String title; 
    @Enumerated(EnumType.STRING)
    private ChatRoomType type; 
	private LocalDateTime lastmessagetime;
//    @OneToMany(mappedBy = "chatroom")
//    private List<ChatMessage> messages = new ArrayList<>();

    // DTO에서 엔티티로 변환하는 메서드
    public static ChatRoom toEntity(ChatRoomDto chatRoomDto) {
        return ChatRoom.builder()
                .id(chatRoomDto.getId())  // ChatRoomDto의 id 사용
                .title(chatRoomDto.getTitle())  // ChatRoomDto의 name 사용
                .type(chatRoomDto.getType())  // ChatRoomDto의 type 사용
                .lastmessagetime(chatRoomDto.getLastmessagetime())
                .build();  // 빌더 패턴을 사용하여 ChatRoom 객체 생성
    }
}