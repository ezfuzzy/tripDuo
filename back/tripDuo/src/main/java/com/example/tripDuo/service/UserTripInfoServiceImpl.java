package com.example.tripDuo.service;

import com.example.tripDuo.dto.UserSavedCourseDto;
import com.example.tripDuo.dto.UserSavedPlaceDto;
import com.example.tripDuo.dto.UserTripInfoDto;
import com.example.tripDuo.dto.UserVisitedPlaceDto;
import com.example.tripDuo.entity.Place;
import com.example.tripDuo.entity.Post;
import com.example.tripDuo.entity.UserSavedCourse;
import com.example.tripDuo.entity.UserSavedPlace;
import com.example.tripDuo.entity.UserTripInfo;
import com.example.tripDuo.entity.UserVisitedPlace;
import com.example.tripDuo.repository.PlaceRepository;
import com.example.tripDuo.repository.PostRepository;
import com.example.tripDuo.repository.UserSavedCourseRepository;
import com.example.tripDuo.repository.UserSavedPlaceRepository;
import com.example.tripDuo.repository.UserTripInfoRepository;
import com.example.tripDuo.repository.UserVisitedPlaceRepository;

import jakarta.persistence.EntityNotFoundException;

public class UserTripInfoServiceImpl implements UserTripInfoService {

	private final UserTripInfoRepository userTripInfoRepo;
	private final UserVisitedPlaceRepository userVisitedPlaceRepo;
	private final UserSavedPlaceRepository userSavedPlaceRepo;
	private final UserSavedCourseRepository userSavedCourseRepo;
	
	private final PlaceRepository placeRepo;
	private final PostRepository postRepo;
	
	public UserTripInfoServiceImpl (UserTripInfoRepository userTripInfoRepo,
			UserVisitedPlaceRepository userVisitedPlaceRepo,
			UserSavedPlaceRepository userSavedPlaceRepo,
			UserSavedCourseRepository userSavedCourseRepo,
			PlaceRepository placeRepo, PostRepository postRepo) {
		
		this.userTripInfoRepo = userTripInfoRepo;
		this.userVisitedPlaceRepo = userVisitedPlaceRepo;
		this.userSavedPlaceRepo = userSavedPlaceRepo;
		this.userSavedCourseRepo = userSavedCourseRepo;
		this.placeRepo = placeRepo;
		this.postRepo = postRepo;
	}
	
	@Override
	public UserTripInfo getTripInfoByUserId(Long userId) {
		
		return userTripInfoRepo.findByUserId(userId);
	}

	@Override
	public void updateTripInfo(UserTripInfoDto userTripInfoDto) {
		
		/**
UserTripInfo.toEntity(UserTripInfoDto dto, UserTripInfo userTripInfoDto, Place place, Post course)
		 */
		
		userTripInfoRepo.save(UserTripInfo.toEntity(userTripInfoDto, null, null, null));
	}

	// ### visit place 
	
	@Override
	public void saveVisitedPlace(UserVisitedPlaceDto userVisitedPlaceDto) {
		
		UserTripInfo userTripInfo = userTripInfoRepo.findById(userVisitedPlaceDto.getTripId())
				.orElseThrow(() -> new EntityNotFoundException("UserTripInfo not found"));

		Place place = placeRepo.findById(userVisitedPlaceDto.getPlaceId())
				.orElseThrow(() -> new EntityNotFoundException("Place not found"));
		
		userVisitedPlaceRepo.save(UserVisitedPlace.toEntity(userVisitedPlaceDto, userTripInfo, place));
	}

	@Override
	public void updateVisitedPlace(UserVisitedPlaceDto userVisitedPlaceDto) {
		UserTripInfo userTripInfo = userTripInfoRepo.findById(userVisitedPlaceDto.getTripId())
				.orElseThrow(() -> new EntityNotFoundException("UserTripInfo not found"));

		Place place = placeRepo.findById(userVisitedPlaceDto.getPlaceId())
				.orElseThrow(() -> new EntityNotFoundException("Place not found"));
		
		userVisitedPlaceRepo.save(UserVisitedPlace.toEntity(userVisitedPlaceDto, userTripInfo, place));
	}

	@Override
	public void deleteVisitedPlace(Long userVisitedPlaceId) {
		userVisitedPlaceRepo.deleteById(userVisitedPlaceId);
	}

	// ### save place  
	
	@Override
	public void savePlaceToMyTripInfo(UserSavedPlaceDto userSavedPlaceDto) {
		UserTripInfo userTripInfo = userTripInfoRepo.findById(userSavedPlaceDto.getTripId())
				.orElseThrow(() -> new EntityNotFoundException("UserTripInfo not found"));

		Place place = placeRepo.findById(userSavedPlaceDto.getPlaceId())
				.orElseThrow(() -> new EntityNotFoundException("Place not found"));
		
		userSavedPlaceRepo.save(UserSavedPlace.toEntity(userSavedPlaceDto, userTripInfo, place));
	}

	@Override
	public void updatePlaceToMyTripInfo(UserSavedPlaceDto userSavedPlaceDto) {
		UserTripInfo userTripInfo = userTripInfoRepo.findById(userSavedPlaceDto.getTripId())
				.orElseThrow(() -> new EntityNotFoundException("UserTripInfo not found"));

		Place place = placeRepo.findById(userSavedPlaceDto.getPlaceId())
				.orElseThrow(() -> new EntityNotFoundException("Place not found"));
		
		userSavedPlaceRepo.save(UserSavedPlace.toEntity(userSavedPlaceDto, userTripInfo, place));
	}

	@Override
	public void deletePlaceFromMyTripInfo(Long userSavedPlaceId) {
		userSavedPlaceRepo.deleteById(userSavedPlaceId);
	}

	// ### save course 
	
	@Override
	public void saveCourseToMyTripInfo(UserSavedCourseDto userSavedCourseDto) {
		UserTripInfo userTripInfo = userTripInfoRepo.findById(userSavedCourseDto.getTripId())
				.orElseThrow(() -> new EntityNotFoundException("UserTripInfo not found"));

		Post course = postRepo.findById(userSavedCourseDto.getCourseId())
				.orElseThrow(() -> new EntityNotFoundException("Course(post) not found"));
		
		userSavedCourseRepo.save(UserSavedCourse.toEntity(userSavedCourseDto, userTripInfo, course));
	}

	@Override
	public void updateCourseToMyTripInfo(UserSavedCourseDto userSavedCourseDto) {
		UserTripInfo userTripInfo = userTripInfoRepo.findById(userSavedCourseDto.getTripId())
				.orElseThrow(() -> new EntityNotFoundException("UserTripInfo not found"));

		Post course = postRepo.findById(userSavedCourseDto.getCourseId())
				.orElseThrow(() -> new EntityNotFoundException("Course(post) not found"));
		
		userSavedCourseRepo.save(UserSavedCourse.toEntity(userSavedCourseDto, userTripInfo, course));
		
	}

	@Override
	public void deleteCourseFromMyTripInfo(Long userSavedCourseId) {
		userSavedCourseRepo.deleteById(userSavedCourseId);
	}

}
