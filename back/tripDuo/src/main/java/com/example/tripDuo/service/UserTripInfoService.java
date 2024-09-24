package com.example.tripDuo.service;

import com.example.tripDuo.dto.UserTripInfoDto;

public interface UserTripInfoService {
	
	// ### trip info ###
	public UserTripInfoDto getTripInfoByUserId(Long userId);
	public void updateTripInfo(UserTripInfoDto userTripInfoDto);

	// ### saved courses ###
	
	
	// ### saved places ###
	
	
}
