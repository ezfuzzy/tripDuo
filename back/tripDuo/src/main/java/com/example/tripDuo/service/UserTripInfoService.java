package com.example.tripDuo.service;

import java.util.List;

import com.example.tripDuo.dto.PlaceDto;
import com.example.tripDuo.dto.UserSavedCourseDto;
import com.example.tripDuo.dto.UserSavedPlaceDto;
import com.example.tripDuo.dto.UserTripInfoDto;
import com.example.tripDuo.dto.UserVisitedPlaceDto;
import com.example.tripDuo.entity.UserSavedPlace;
import com.example.tripDuo.entity.UserTripInfo;

public interface UserTripInfoService {
	
	// ### trip info ###
	public UserTripInfo getTripInfoByUserId(Long userId);
	public void updateTripInfo(UserTripInfoDto userTripInfoDto);	
	
	// ### visited places ###
	public void saveVisitedPlace(PlaceDto placeDto);
	public List<UserVisitedPlaceDto> getVisitedPlaceList(Long userId);
	public void updateUserVisitedPlace(UserVisitedPlaceDto userVisitedPlaceDto);
	public void deleteVisitedPlace(Long userVisitedPlaceId);
	
	// ### saved places ###
	public UserSavedPlace savePlaceToMyTripInfo(PlaceDto placeDto);
	public List<UserSavedPlace> getSavedPlaceList(Long userId);
	public void updateUserSavedPlace(UserSavedPlaceDto userSavedPlaceDto);
	public void deletePlaceFromMyTripInfo(Long userSavedPlaceId);
	
	// ### saved courses ###
	public void saveCourseToMyTripInfo(UserSavedCourseDto userSavedCourseDto);
	public List<UserSavedCourseDto> getSavedCourseList(Long userId);
	public void updateCourseToMyTripInfo(UserSavedCourseDto userSavedCourseDto);
	public void deleteCourseFromMyTripInfo(Long userSavedCourseId);
}
