package com.example.tripDuo.dto;

import jakarta.persistence.Column;
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

    private Long maxBudget;
    private Long minBudget;

    private String[] tripStyle;
    private String[] languages;
    private Boolean smoking;

    private String[] savedPlaces;
    private String[] savedCourses;

    // toDto
}
