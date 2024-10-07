package com.example.tripDuo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tripDuo.dto.ReportDto;
import com.example.tripDuo.enums.ReportTarget;
import com.example.tripDuo.service.ReportService;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}

	// 어떤 유저(reporterId)가 다른 대상(targetId, targetType)을 신고하기
	@PostMapping("/{targetId}/{reportTarget}/{reporterId}")
	public Map<String, Object> report(@PathVariable("targetId") Long targetId,
			@PathVariable("reportTarget") String target,		
			@PathVariable("reporterId") Long reporterId,
			@RequestBody ReportDto userReportDto) {

		ReportTarget targetEnum;

		try {
			targetEnum = ReportTarget.fromString(target);
		} catch (IllegalArgumentException e) {
			return Map.of("isSuccess", false, "message", e.getMessage());
		}
		
		userReportDto.setReporterId(reporterId);
		Map<String, Object> result = reportService.report(userReportDto, targetEnum, targetId);
		return result;
	}

	// 신고 목록 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getReportList(ReportDto reportDto) {

        return ResponseEntity.ok(reportService.getReportList(reportDto));
    }

	// 신고 처리
	@PutMapping("/{reportId}")
	public ResponseEntity<String> processReport(@PathVariable Long reportId, @RequestBody ReportDto reportDto) {
		
		reportDto.setId(reportId);
		reportService.processReport(reportDto);
		return ResponseEntity.ok("Account status updated successfully");
	}
}