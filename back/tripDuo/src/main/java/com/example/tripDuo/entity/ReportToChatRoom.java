package com.example.tripDuo.entity;

import com.example.tripDuo.dto.ReportDto;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Entity
// target_type 컬럼에 CHAT_ROOM 을 저장
@DiscriminatorValue("CHAT_ROOM")
public class ReportToChatRoom extends Report {

    @ManyToOne
    @JoinColumn(name = "reported_chat_room_id")
    private ChatRoom reportedChatRoom;

    public static ReportToChatRoom toEntity(ReportDto dto, ChatRoom chatRoom) {
        return ReportToChatRoom.builder()
                .id(dto.getId())
                .reporterId(dto.getReporterId() != null ? dto.getReporterId() : 0L)
                .reportedContentOwnerId(dto.getReportedUserId() != null ? dto.getReportedUserId() : 0L)
                .content(dto.getContent())
                .status(dto.getReportStatus())
                .createdAt(dto.getCreatedAt())
                .reportedChatRoom(chatRoom)
                .build();
    }
}