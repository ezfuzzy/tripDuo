package com.example.tripDuo.enums;

public enum Gender {
    MALE,       // 남성
    FEMALE;		// 여성
	
	public static Gender fromString(String gender) {
        try {
            return Gender.valueOf(gender.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid gender: " + gender, e);
        }
    }
}
