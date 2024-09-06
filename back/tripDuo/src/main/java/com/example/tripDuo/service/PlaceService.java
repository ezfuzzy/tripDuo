package com.example.tripDuo.service;

import java.util.List;

import com.example.tripDuo.dto.PlaceDto;

public interface PlaceService {
	
	
	// ### get place ###
	public List<PlaceDto> getPlaceList();
	public PlaceDto getPlaceById(Long id);
	
	// ### new place ###
	public void addPlace(PlaceDto dto);
	
	// ### update place ###
	public PlaceDto updatePlace(PlaceDto dto);
	
	// ### delete place ###
	public void deletePlace(Long id);
}
