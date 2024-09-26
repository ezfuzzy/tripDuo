package com.example.tripDuo.dto;

import com.example.tripDuo.entity.UserProfileInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {
	private Long id;
	private UserProfileInfo user;
	private String message;
	private String timestamp;
}
