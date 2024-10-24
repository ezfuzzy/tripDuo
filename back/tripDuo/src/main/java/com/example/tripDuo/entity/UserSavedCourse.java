package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.UserSavedCourseDto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name="user_saved_courses")
public class UserSavedCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post course;

    @Column(length = 100)
	private String userMemo;
    private LocalDateTime createdAt;
    
    @PrePersist
    public void onPrePersist() {
        createdAt = LocalDateTime.now();
    }
    
    // toEntity 
    public static UserSavedCourse toEntity(UserSavedCourseDto dto, Post course) {
    	
    	return UserSavedCourse.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .course(course)
                .userMemo(dto.getUserMemo())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
