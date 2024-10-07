package com.example.tripDuo.dto;

import java.time.LocalDateTime;
import java.time.YearMonth;

import com.example.tripDuo.entity.Report;
import com.example.tripDuo.entity.ReportToChatMessage;
import com.example.tripDuo.entity.ReportToChatRoom;
import com.example.tripDuo.entity.ReportToPost;
import com.example.tripDuo.entity.ReportToPostComment;
import com.example.tripDuo.entity.ReportToUser;
import com.example.tripDuo.entity.ReportToUserReview;
import com.example.tripDuo.enums.AccountStatus;
import com.example.tripDuo.enums.ReportStatus;
import com.example.tripDuo.enums.ReportTarget;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class ReportDto {
    private Long id;
    private Long reporterId;
    private String content;
    private ReportStatus reportStatus;
    private LocalDateTime createdAt;

    private String targetType;
    private Long targetId;

    // ### for app ###
    private int pageNum = 1;
    private String sortBy;
    private AccountStatus accountStatus;
    private YearMonth createdAtMonth;

    public static ReportDto toDto(Report report) {
        ReportDto dto = ReportDto.builder()
            .id(report.getId())
            .reporterId(report.getReporterId())
            .content(report.getContent())
            .reportStatus(report.getStatus())
            .createdAt(report.getCreatedAt())
            .build();

        // 각 Report 타입에 따라 targetType과 targetId 설정
        if (report instanceof ReportToUser) {
            setTargetInfo(dto, ReportTarget.USER, ((ReportToUser) report).getReportedUser().getId());
        } else if (report instanceof ReportToUserReview) {
            setTargetInfo(dto, ReportTarget.USER_REVIEW, ((ReportToUserReview) report).getReportedUserReview().getId());
        } else if (report instanceof ReportToPost) {
            setTargetInfo(dto, ReportTarget.POST, ((ReportToPost) report).getReportedPost().getId());
        } else if (report instanceof ReportToPostComment) {
            setTargetInfo(dto, ReportTarget.POST_COMMENT, ((ReportToPostComment) report).getReportedPostComment().getId());
        } else if (report instanceof ReportToChatRoom) {
            setTargetInfo(dto, ReportTarget.CHAT_ROOM, ((ReportToChatRoom) report).getReportedChatRoom().getId());
        } else if (report instanceof ReportToChatMessage) {
            setTargetInfo(dto, ReportTarget.CHAT_MESSAGE, ((ReportToChatMessage) report).getReportedChatMessage().getId());
        }

        return dto;
    }

    // targetType과 targetId를 설정하는 헬퍼 메서드
    private static void setTargetInfo(ReportDto dto, ReportTarget target, Long targetId) {
        dto.setTargetType(target.name());
        dto.setTargetId(targetId);
    }
}