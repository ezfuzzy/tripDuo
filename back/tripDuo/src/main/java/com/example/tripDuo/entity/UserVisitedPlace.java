package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.UserVisitedPlaceDto;

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
@Table(name="user_visited_places")
public class UserVisitedPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Long userId;

    @Column(length = 100)
    private String userMemo;
    
    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    private LocalDateTime visitDate;
    
    
    // toEntity 
    public static UserVisitedPlace toEntity(UserVisitedPlaceDto dto, Place place) {
    	
    	return UserVisitedPlace.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .userMemo(dto.getUserMemo())
                .place(place)
                .visitDate(dto.getVisitDate())
                .build();
    }
}
