package com.example.tripDuo.enums;

public enum AccountStatus {
    ACTIVE,      // 활성 상태
    INACTIVE,    // 비활성 상태 - 로그인한지 일정 기간 지나면 
    WARNED,      // 경고 상태 - 로그인 가능
    SUSPENDED;    // 정지 상태 - 로그인 불가

    public static AccountStatus fromString(String type) {
        try {
            return AccountStatus.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account status: " + type, e);
        }
    }
}