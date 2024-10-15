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

    private List<Long> visitedPlaces;
    private List<Long> savedPlaces;
    private List<Long> savedCourses;

    // toDto
    static public UserTripInfoDto toDto(UserTripInfo entity) {
    	
        return UserTripInfoDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .tripStyle(entity.getTripStyle())
                .languages(entity.getLanguages())
                .smoking(entity.getSmoking())
                .visitedPlaces(entity.getVisitedPlaces())
                .savedPlaces(entity.getSavedPlaces())
                .savedCourses(entity.getSavedCourses())
                .build();
    }
}
