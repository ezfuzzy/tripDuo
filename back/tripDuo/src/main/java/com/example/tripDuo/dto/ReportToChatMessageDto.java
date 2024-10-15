package com.example.tripDuo.dto;

import com.example.tripDuo.entity.ReportToChatMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class ReportToChatMessageDto extends ReportDto {
    private Long reportedChatMessageId;

    public static ReportToChatMessageDto toDto(ReportToChatMessage entity) {
        return ReportToChatMessageDto.builder()
                .id(entity.getId())
                .reporterId(entity.getReporterId())
                .content(entity.getContent())
                .reportStatus(entity.getStatus())
                .createdAt(entity.getCreatedAt())   
                .reportedChatMessageId(entity.getReportedChatMessage() != null ? entity.getReportedChatMessage().getId() : null)
                .build();
    }
}