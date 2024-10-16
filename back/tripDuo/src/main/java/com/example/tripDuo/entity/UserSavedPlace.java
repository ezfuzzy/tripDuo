package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.UserSavedPlaceDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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

    private Long userId;
    private String userMemo;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    private String di;
    private LocalDateTime createdAt;
    
    @PrePersist
    public void onPrePersist() {
        createdAt = LocalDateTime.now();
    }
    
    // toEntity 
    public static UserSavedPlace toEntity(UserSavedPlaceDto dto, Place place) {
    	
    	return UserSavedPlace.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .userMemo(dto.getUserMemo())
                .place(place)
                .di(dto.getDi())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
