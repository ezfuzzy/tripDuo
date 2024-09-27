package com.example.tripDuo.enums;

public enum FollowType {
	FOLLOW,   // 내가 상대방을 팔로우한 상태
	BLOCK,    // 내가 차단한 상태
	ISBLOCKED;  // 내가 차단당한 상태
	
    public static FollowType fromString(String type) {
        try {
            return FollowType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid follow type: " + type, e);
        }
    }
}
