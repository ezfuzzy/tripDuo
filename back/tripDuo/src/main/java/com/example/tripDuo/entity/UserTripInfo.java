package com.example.tripDuo.entity;

import java.util.List;

import com.example.tripDuo.dto.UserTripInfoDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name="user_trip_infos")
public class UserTripInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private long userId;

    @Column(columnDefinition = "TEXT[]")
    private String[] tripStyle;

    @Column(columnDefinition = "TEXT[]")
    private String[] languages;
    private Boolean smoking;

    private List<Long> visitedPlaces;
    private List<Long> savedPlaces;
    private List<Long> savedCourses;

    // toEntity
    static public UserTripInfo toEntity(UserTripInfoDto dto) {
    	
        return UserTripInfo.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .tripStyle(dto.getTripStyle())
                .languages(dto.getLanguages())
                .smoking(dto.getSmoking())
                .visitedPlaces(dto.getVisitedPlaces())
                .savedPlaces(dto.getSavedPlaces())
                .savedCourses(dto.getSavedCourses())
                .build();
    }
}
