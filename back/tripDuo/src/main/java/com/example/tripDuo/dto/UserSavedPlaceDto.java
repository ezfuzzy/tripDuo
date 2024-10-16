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
    private String userMemo;
    private Long placeId;
    private String placeName;
    private String di;
    private LocalDateTime createdAt;
    
    //	toDto
    public static UserSavedPlaceDto toDto(UserSavedPlace entity) {
    	
    	return UserSavedPlaceDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .userMemo(entity.getUserMemo())
                .placeId(entity.getPlace() != null ? entity.getPlace().getId() : null)
                .placeName(entity.getPlace() != null ? entity.getPlace().getPlaceName() : null)
                .di(entity.getDi())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
