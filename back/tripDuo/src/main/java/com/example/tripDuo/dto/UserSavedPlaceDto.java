package com.example.tripDuo.dto;

import java.time.LocalDateTime;

import com.example.tripDuo.entity.UserSavedPlace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserSavedPlaceDto {

    private Long id;
    private Long userId;
    private Long placeId;
    private String placeName;
    private LocalDateTime createdAt;
    
    //	toDto
    public static UserSavedPlaceDto toDto(UserSavedPlace entity) {
    	
    	return UserSavedPlaceDto.builder()
                .id(entity.getId())
                .userId(entity.getUserTripInfo() != null ? entity.getUserTripInfo().getUserId() : null)
                .placeId(entity.getPlace() != null ? entity.getPlace().getId() : null)
                .placeName(entity.getPlace() != null ? entity.getPlace().getPlaceName() : null)
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
