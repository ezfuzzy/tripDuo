package com.example.tripDuo.service;

import com.example.tripDuo.dto.ReportDto;
import com.example.tripDuo.enums.ReportTarget;

public interface ReportService {
    public ReportDto report(ReportDto userReportDto, ReportTarget targetType, Long reportedTargetId);
}