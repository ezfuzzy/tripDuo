package com.example.tripDuo.dto;

import java.time.LocalDateTime;

import com.example.tripDuo.entity.UserSavedCourse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserSavedCourseDto {
	private Long id;
    private Long userId;
    private Long courseId;
	private String courseTitle; 
	private String userMemo;
	private LocalDateTime createdAt;
	
	// toDto
	public static UserSavedCourseDto toDto(UserSavedCourse entity) {
		
    	return UserSavedCourseDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .courseId(entity.getCourse() != null ? entity.getCourse().getId() : null)
                .courseTitle(entity.getCourse() != null ? entity.getCourse().getTitle() : null)
                .userMemo(entity.getUserMemo())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
