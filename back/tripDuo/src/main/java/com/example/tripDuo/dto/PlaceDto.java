package com.example.tripDuo.dto;

import com.example.tripDuo.entity.Place;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PlaceDto {
	
	private Long id;
	
    // ### kakaoMap, googleMap 에서 제공하는 정보 ###
    
    private String addressName;
    private String categoryGroupCode;
    private String categoryGroupName;
    private String categoryName;
    private String placeId;
    private String phone;
    private String placeName;
    private String placeUrl;
    private String roadAddressName;

    private double latitude;
    private double longitude;
    
    // ### image, 설명, 특징, 태그 ...
    
    
    // toDto 메소드
    public static PlaceDto toDto(Place entity) {
        return PlaceDto.builder()
                .id(entity.getId())
                .addressName(entity.getAddressName())
                .categoryGroupCode(entity.getCategoryGroupCode())
                .categoryGroupName(entity.getCategoryGroupName())
                .categoryName(entity.getCategoryName())
                .placeId(entity.getPlaceId())
                .phone(entity.getPhone())
                .placeName(entity.getPlaceName())
                .placeUrl(entity.getPlaceUrl())
                .roadAddressName(entity.getRoadAddressName())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .build();
    }
    
}