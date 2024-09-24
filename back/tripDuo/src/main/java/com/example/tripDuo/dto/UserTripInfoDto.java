package com.example.tripDuo.dto;

import java.util.List;

import com.example.tripDuo.entity.UserTripInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserTripInfoDto {
    private Long id;

    private Long userId;

    private String[] tripStyle;
    private String[] languages;
    
    private Boolean smoking;

    private List<UserVisitedPlaceDto> visitedPlaces;
    private List<UserSavedPlaceDto> savedPlaces;
    private List<UserSavedCourseDto> savedCourses;

    // toDto
    static public UserTripInfoDto toDto(UserTripInfo entity) {
    	
        return UserTripInfoDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .tripStyle(entity.getTripStyle())
                .languages(entity.getLanguages())
                .smoking(entity.getSmoking())
                .visitedPlaces(entity.getVisitedPlaces().stream()
                        .map(UserVisitedPlaceDto::toDto)
                        .toList()) // UserVisitedPlaceDto의 toDto 메서드를 사용하여 변환
                .savedPlaces(entity.getSavedPlaces().stream()
                        .map(UserSavedPlaceDto::toDto)
                        .toList()) // UserSavedPlaceDto의 toDto 메서드를 사용하여 변환
                .savedCourses(entity.getSavedCourses().stream()
                        .map(UserSavedCourseDto::toDto)
                        .toList()) // UserSavedCourseDto의 toDto 메서드를 사용하여 변환
                .build();
    }
}
