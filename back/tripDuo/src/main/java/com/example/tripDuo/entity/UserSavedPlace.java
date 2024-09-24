package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.UserSavedPlaceDto;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="user_saved_places")
public class UserSavedPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_trip_info_id")
    private UserTripInfo userTripInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    private LocalDateTime createdAt;
    
    // toEntity 
    public static UserSavedPlace toEntity(UserSavedPlaceDto dto, UserTripInfo userTripInfo, Place place) {
    	
    	return UserSavedPlace.builder()
                .id(dto.getId())
                .userTripInfo(userTripInfo)
                .place(place)
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
