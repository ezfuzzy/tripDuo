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
// target_type 컬럼에 CHAT_MESSAGE 을 저장
@DiscriminatorValue("CHAT_MESSAGE")
public class ReportToChatMessage extends Report {

    @ManyToOne
    @JoinColumn(name = "reported_chat_message_id")
    private ChatMessage reportedChatMessage;

    public static ReportToChatMessage toEntity(ReportDto dto, ChatMessage chatMessage) {
        return ReportToChatMessage.builder()
                .id(dto.getId())
                .reporterId(dto.getReporterId() != null ? dto.getReporterId() : 0L)
                .reportedContentOwnerId(dto.getReportedUserId() != null ? dto.getReportedUserId() : 0L)
                .content(dto.getContent())
                .status(dto.getReportStatus())
                .createdAt(dto.getCreatedAt())
                .reportedChatMessage(chatMessage)
                .build();
    }
}