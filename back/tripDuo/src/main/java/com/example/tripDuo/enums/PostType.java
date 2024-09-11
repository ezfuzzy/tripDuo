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
            throw new IllegalArgumentException("Invalid post type: " + type, e);
        }
    }
}