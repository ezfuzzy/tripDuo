package com.example.tripDuo.service;

import java.util.Map;

import com.example.tripDuo.dto.ReportDto;
import com.example.tripDuo.enums.ReportTarget;

public interface ReportService {
    public Map<String, Object> report(ReportDto userReportDto, ReportTarget targetType, Long reportedTargetId);
    public Map<String, Object> getReportList(ReportDto reportDto);
    public void processReport(ReportDto reportDto);
}