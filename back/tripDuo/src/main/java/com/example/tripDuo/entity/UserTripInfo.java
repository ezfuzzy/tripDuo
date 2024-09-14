package com.example.tripDuo.entity;

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
@Table(name="user_trip_infos")
public class UserTripInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private long userId;

    private Long maxBudget;
    private Long minBudget;

    @Column(columnDefinition = "TEXT[]")
    private String[] tripStyle;

    @Column(columnDefinition = "TEXT[]")
    private String[] languages;
    private Boolean smoking;

    @Column(columnDefinition = "TEXT[]")
    private String[] savedPlaces;

    @Column(columnDefinition = "TEXT[]")
    private String[] savedCourses;

    // toEntity
}
