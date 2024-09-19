package com.example.tripDuo.enums;

public enum CommentStatus {
    PUBLIC,     // 공개
    PRIVATE,      // 비밀댓글
    DELETED;      // 삭제됨
    
    public static CommentStatus fromString(String status) {
        try {
            return CommentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid comment status: " + status, e);
        }
    }
}
