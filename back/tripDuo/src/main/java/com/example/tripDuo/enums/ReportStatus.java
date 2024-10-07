package com.example.tripDuo.enums;

public enum ReportStatus {
    PROCESSED, // 처리됨
    UNPROCESSED, // 처리되지 않음
    PENDING; // 보류 중

    public static ReportStatus fromString(String status) {
        try {
            return ReportStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid report status: " + status, e);
        }
    }
}