package com.example.tripDuo.entity;

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
@Table(name = "places")
public class Place {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}