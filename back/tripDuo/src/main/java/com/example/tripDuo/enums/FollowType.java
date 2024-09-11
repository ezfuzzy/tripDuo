package com.example.tripDuo.enums;

public enum FollowType {
	FOLLOW,   // 팔로우
	BLOCK;    // 차단
	
    public static FollowType fromString(String type) {
        try {
            return FollowType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid follow type: " + type, e);
        }
    }
}
