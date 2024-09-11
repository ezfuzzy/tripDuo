package com.example.tripDuo.enums;

public enum PostType {
    MATE,       // 매이트
    COURSE,     // 코스
    TRIP_LOG,     // 여행기
    COMMUNITY;	// 커뮤니티
    
    public static PostType fromString(String type) {
        try {
            return PostType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 예외 처리 로직, 예를 들어 기본값 설정 또는 로그 기록
            throw new IllegalArgumentException("Invalid post type: " + type, e);
        }
    }
}