package com.example.tripDuo.dto;

import com.example.tripDuo.entity.ReportToChatRoom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class ReportToChatRoomDto extends ReportDto {
    private Long reportedChatRoomId;

    public static ReportToChatRoomDto toDto(ReportToChatRoom entity) {
        return ReportToChatRoomDto.builder()
                .id(entity.getId())
                .reporterId(entity.getReporterId())
                .content(entity.getContent())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())   
                .reportedChatRoomId(entity.getReportedChatRoom() != null ? entity.getReportedChatRoom().getId() : null)
                .build();
    }
}   