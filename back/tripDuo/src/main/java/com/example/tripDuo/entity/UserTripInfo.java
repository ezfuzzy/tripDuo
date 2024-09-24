package com.example.tripDuo.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.tripDuo.dto.UserTripInfoDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "userTripInfo", cascade = CascadeType.ALL)
    private List<UserVisitedPlace> visitedPlaces = new ArrayList<>();

    @OneToMany(mappedBy = "userTripInfo", cascade = CascadeType.ALL)
    private List<UserSavedPlace> savedPlaces = new ArrayList<>();

    @OneToMany(mappedBy = "userTripInfo", cascade = CascadeType.ALL)
    private List<UserSavedCourse> savedCourses = new ArrayList<>();

    // toEntity
    static public UserTripInfo toEntity(UserTripInfoDto dto, Place place, Post course) {
    	
        return UserTripInfo.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .tripStyle(dto.getTripStyle())
                .languages(dto.getLanguages())
                .smoking(dto.getSmoking())
                .visitedPlaces(dto.getVisitedPlaces().stream()
                        .map(dtoItem -> UserVisitedPlace.toEntity(dtoItem, place))
                        .toList())
                .savedPlaces(dto.getSavedPlaces().stream()
                        .map(dtoItem -> UserSavedPlace.toEntity(dtoItem, place))
                        .toList())
                .savedCourses(dto.getSavedCourses().stream()
                        .map(dtoItem -> UserSavedCourse.toEntity(dtoItem, course))
                        .toList())
                .build();
    }
}
