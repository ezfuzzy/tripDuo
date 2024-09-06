package com.example.tripDuo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tripDuo.dto.PlaceDto;
import com.example.tripDuo.entity.Place;
import com.example.tripDuo.repository.PlaceRepository;

@Service
public class PlaceServiceImpl implements PlaceService{

	@Autowired
	private PlaceRepository placeRepo;
	
	@Override
	public List<PlaceDto> getPlaceList() {
		return placeRepo.findAll().stream().map(PlaceDto::toDto).toList();
	}

	@Override
	public PlaceDto getPlaceById(Long id) {
		return PlaceDto.toDto(placeRepo.findById(id).get());
	}

	@Override
	public void addPlace(PlaceDto dto) {
		placeRepo.save(Place.toEntity(dto));
	}

	@Override
	public PlaceDto updatePlace(PlaceDto dto) {
		placeRepo.save(Place.toEntity(dto));
		return dto;
	}

	@Override
	public void deletePlace(Long id) {
		placeRepo.deleteById(id);
	}

}
