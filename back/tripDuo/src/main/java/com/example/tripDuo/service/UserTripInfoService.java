package com.example.tripDuo.service;

import com.example.tripDuo.dto.UserSavedCourseDto;
import com.example.tripDuo.dto.UserSavedPlaceDto;
import com.example.tripDuo.dto.UserTripInfoDto;
import com.example.tripDuo.dto.UserVisitedPlaceDto;
import com.example.tripDuo.entity.UserTripInfo;

public interface UserTripInfoService {
	
	// ### trip info ###
	public UserTripInfo getTripInfoByUserId(Long userId);
	public void updateTripInfo(UserTripInfoDto userTripInfoDto);	
	
	// ### saved places ###
	public void saveVisitedPlace(UserVisitedPlaceDto userVisitedPlaceDto);
	public void updateVisitedPlace(UserVisitedPlaceDto userVisitedPlaceDto);
	public void deleteVisitedPlace(Long userVisitedPlaceId);
	
	// ### saved places ###
	public void savePlaceToMyTripInfo(UserSavedPlaceDto userSavedPlaceDto);
	public void updatePlaceToMyTripInfo(UserSavedPlaceDto userSavedPlaceDto);
	public void deletePlaceFromMyTripInfo(Long userSavedPlaceId);
	
	// ### saved courses ###
	public void saveCourseToMyTripInfo(UserSavedCourseDto userSavedCourseDto);
	public void updateCourseToMyTripInfo(UserSavedCourseDto userSavedCourseDto);
	public void deleteCourseFromMyTripInfo(Long userSavedCourseId);
}
