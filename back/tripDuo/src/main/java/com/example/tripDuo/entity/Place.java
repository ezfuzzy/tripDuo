package com.example.tripDuo.entity;

import com.example.tripDuo.dto.PlaceDto;

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
@Table(name="places", indexes = {
	@Index(name = "idx_places_map_place_id", columnList = "mapPlaceId")
}) 
public class Place {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
    // ### kakaoMap, googleMap 에서 제공하는 정보 ###

    @Column(length = 50)
    private String addressName;

    @Column(length = 50)
    private String categoryGroupCode;

    @Column(length = 50)
    private String categoryGroupName;

    @Column(length = 50)
    private String categoryName;

    @Column(length = 50)
    private String mapPlaceId;

    @Column(length = 50)
    private String phone;

    @Column(length = 50)
    private String placeName;

    @Column(length = 50)
    private String placeUrl;

    @Column(length = 50)
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