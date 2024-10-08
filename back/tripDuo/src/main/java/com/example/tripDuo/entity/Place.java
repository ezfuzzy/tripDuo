package com.example.tripDuo.entity;

import com.example.tripDuo.dto.PlaceDto;

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
@Table(name="places") // 인덱스 추가 
public class Place {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
    // ### kakaoMap, googleMap 에서 제공하는 정보 ###
    
    private String addressName;
    private String categoryGroupCode;
    private String categoryGroupName;
    private String categoryName;
    private String mapPlaceId;
    private String phone;
    private String placeName;
    private String placeUrl;
    private String roadAddressName;

    private double latitude;
    private double longitude;
    
    // ### image, 설명, 특징, 태그 ...
    
    
    // ### toEntity ###
    
    public static Place toEntity(PlaceDto dto) {
        return Place.builder()
                .id(dto.getId())
                .addressName(dto.getAddressName())
                .categoryGroupCode(dto.getCategoryGroupCode())
                .categoryGroupName(dto.getCategoryGroupName())
                .categoryName(dto.getCategoryName())
                .mapPlaceId (dto.getMapPlaceId())
                .phone(dto.getPhone())
                .placeName(dto.getPlaceName())
                .placeUrl(dto.getPlaceUrl())
                .roadAddressName(dto.getRoadAddressName())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }
    
}