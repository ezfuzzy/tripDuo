package com.example.tripDuo.dto;

import java.time.LocalDateTime;

import com.example.tripDuo.entity.UserVisitedPlace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserVisitedPlaceDto {

    private Long id;
    private Long userId;
    private Long placeId;
    private String placeName;  
    private LocalDateTime visitDate;

    //	toDto
    public static UserVisitedPlaceDto toDto(UserVisitedPlace entity) {
    	
    	return UserVisitedPlaceDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .placeId(entity.getPlace() != null ? entity.getPlace().getId() : null)
                .placeName(entity.getPlace() != null ? entity.getPlace().getPlaceName() : null)
                .visitDate(entity.getVisitDate())
                .build();
    }
}
