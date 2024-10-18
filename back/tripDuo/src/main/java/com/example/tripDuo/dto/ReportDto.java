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
    private Long reportedUserId;

    private String content;
    private ReportStatus reportStatus;
    private LocalDateTime createdAt;

    private String targetType;
    private Long targetId;

    // ### for app ###
    private int pageNum = 1;
    private int pageSize = 10;
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
        if (report instanceof ReportToUser r) {
            dto.setTargetType(ReportTarget.USER.name());
            dto.setTargetId((r).getReportedUser().getId());
        } else if (report instanceof ReportToUserReview r) {
            dto.setTargetType(ReportTarget.USER_REVIEW.name());
            dto.setTargetId((r).getReportedUserReview().getId());
        } else if (report instanceof ReportToPost r) {
            dto.setTargetType(ReportTarget.POST.name());
            dto.setTargetId((r).getReportedPost().getId());
        } else if (report instanceof ReportToPostComment r) {
            dto.setTargetType(ReportTarget.POST_COMMENT.name());
            dto.setTargetId((r).getReportedPostComment().getId());
        } else if (report instanceof ReportToChatRoom r) {
            dto.setTargetType(ReportTarget.CHAT_ROOM.name());
            dto.setTargetId((r).getReportedChatRoom().getId());
        } else if (report instanceof ReportToChatMessage r) {
            dto.setTargetType(ReportTarget.CHAT_MESSAGE.name());
            dto.setTargetId((r).getReportedChatMessage().getId());
        }

        return dto;
    }
}