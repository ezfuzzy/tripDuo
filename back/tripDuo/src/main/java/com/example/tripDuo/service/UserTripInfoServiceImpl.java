package com.example.tripDuo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tripDuo.dto.PlaceDto;
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

@Service
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
		
		userTripInfoRepo.save(UserTripInfo.toEntity(userTripInfoDto));
	}

	// ### visit place 
	
	@Override
	public void saveVisitedPlace(PlaceDto placeDto) {
		
		Place place = placeRepo.findByPlaceId(placeDto.getPlaceId());
		if(place == null) {
			place = placeRepo.save(Place.toEntity(placeDto));
		}
		UserVisitedPlace userVisitedPlace = UserVisitedPlace.builder()
														.userId(placeDto.getUserId())
														.userMemo(placeDto.getUserMemo())
														.place(place)
														.visitDate(placeDto.getVisitDate())
														.build();
		
		userVisitedPlaceRepo.save(userVisitedPlace);
	}

	@Override
	public List<UserVisitedPlaceDto> getVisitedPlaceList(Long userId) {
		return userVisitedPlaceRepo.findByUserId(userId).stream().map(UserVisitedPlaceDto::toDto).toList();
	}

	// 변경될 수 있는건 userMemo + visitDate 
	// id를 비롯한 모든 정보가 프론트에서 넘어옴 
	@Override
	public void updateUserVisitedPlace(UserVisitedPlaceDto userVisitedPlaceDto) {

		Place place = placeRepo.findById(userVisitedPlaceDto.getPlaceId())
				.orElseThrow(() -> new EntityNotFoundException("Place not found"));
		
		userVisitedPlaceRepo.save(UserVisitedPlace.toEntity(userVisitedPlaceDto, place));
	}

	@Override
	public void deleteVisitedPlace(Long userVisitedPlaceId) {
		userVisitedPlaceRepo.deleteById(userVisitedPlaceId);
	}

	// ### save place  
	
	@Override
	public void savePlaceToMyTripInfo(PlaceDto placeDto) {

		Place place = placeRepo.findByPlaceId(placeDto.getPlaceId());
		
		if(place == null) {
			place = placeRepo.save(Place.toEntity(placeDto));
		}
		
		UserSavedPlace userSavedPlace = UserSavedPlace.builder()
														.userId(placeDto.getUserId())
														.userMemo(placeDto.getUserMemo())
														.place(place)
														.build();
		
		userSavedPlaceRepo.save(userSavedPlace);
	}
	
	@Override
	public List<UserSavedPlaceDto> getSavedPlaceList(Long userId) {
		
		return userSavedPlaceRepo.findByUserId(userId).stream().map(UserSavedPlaceDto::toDto).toList();
	}

	
	// 변경될 수 있는건 userMemo
	// id를 비롯한 모든 정보가 프론트에서 넘어옴 
	@Override
	public void updateUserSavedPlace(UserSavedPlaceDto userSavedPlaceDto) {

		Place place = placeRepo.findById(userSavedPlaceDto.getPlaceId())
				.orElseThrow(() -> new EntityNotFoundException("Place not found"));
		
		userSavedPlaceRepo.save(UserSavedPlace.toEntity(userSavedPlaceDto, place));
	}

	@Override
	public void deletePlaceFromMyTripInfo(Long userSavedPlaceId) {
		userSavedPlaceRepo.deleteById(userSavedPlaceId);
	}

	// ### save course 
	
	@Override
	public void saveCourseToMyTripInfo(UserSavedCourseDto userSavedCourseDto) {

		Post course = postRepo.findById(userSavedCourseDto.getCourseId())
				.orElseThrow(() -> new EntityNotFoundException("Course(post) not found"));
		
		userSavedCourseRepo.save(UserSavedCourse.toEntity(userSavedCourseDto, course));
	}
	
	@Override
	public List<UserSavedCourseDto> getSavedCourseList(Long userId) {
		return userSavedCourseRepo.findByUserId(userId).stream().map(UserSavedCourseDto::toDto).toList();
	}

	@Override
	public void updateCourseToMyTripInfo(UserSavedCourseDto userSavedCourseDto) {
		
		Post course = postRepo.findById(userSavedCourseDto.getCourseId())
				.orElseThrow(() -> new EntityNotFoundException("Course(post) not found"));
		
		userSavedCourseRepo.save(UserSavedCourse.toEntity(userSavedCourseDto, course));
		
	}

	@Override
	public void deleteCourseFromMyTripInfo(Long userSavedCourseId) {
		userSavedCourseRepo.deleteById(userSavedCourseId);
	}

}
