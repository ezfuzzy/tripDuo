package com.example.tripDuo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	public ResponseEntity<ReportDto> report(@PathVariable("targetId") Long targetId,
			@PathVariable("reportTarget") String target,		
			@PathVariable("reporterId") Long reporterId,
			@RequestBody ReportDto userReportDto) {

		ReportTarget targetEnum;

		try {
			targetEnum = ReportTarget.fromString(target);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid target type: " + target, e);
		}
		
		userReportDto.setReporterId(reporterId);
		ReportDto insertedUserReportDto = reportService.report(userReportDto, targetEnum, targetId);
		return ResponseEntity.ok(insertedUserReportDto);
	}
}