package com.example.tripDuo.dto;

import java.time.LocalDateTime;

import com.example.tripDuo.enums.ReportStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class UserReportDto {
    private Long id;
    private Long reporterId;
    private String content;
    private ReportStatus status;
    private LocalDateTime createdAt;
}