package com.example.tripDuo.enums;

public enum ReportTarget {
    USER,
    USER_REVIEW,
    POST,
    POST_COMMENT,
    CHAT_ROOM,
    CHAT_MESSAGE;

    public static ReportTarget fromString(String target) {
        try {
            return ReportTarget.valueOf(target.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid target type: " + target, e);
        }
    }
}