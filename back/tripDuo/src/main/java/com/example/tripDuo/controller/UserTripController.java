package com.example.tripDuo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tripDuo.dto.UserSavedCourseDto;
import com.example.tripDuo.dto.UserSavedPlaceDto;
import com.example.tripDuo.dto.UserTripInfoDto;
import com.example.tripDuo.dto.UserVisitedPlaceDto;
import com.example.tripDuo.service.UserTripInfoService;

@RestController
@RequestMapping("/api/v1/users/{userId}/trips")
public class UserTripController {

	private final UserTripInfoService userTripInfoService;

	public UserTripController(UserTripInfoService userTripInfoService) {
		this.userTripInfoService = userTripInfoService;
	}

	// ### trip info ###

	@GetMapping
	public ResponseEntity<?> getTripInfoByUserId(@PathVariable("userId") Long userId) {

		try {
			return ResponseEntity.ok(userTripInfoService.getTripInfoByUserId(userId));
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping
	public ResponseEntity<?> updateTripInfo(@RequestBody UserTripInfoDto userTripInfoDto) {

		try {
			userTripInfoService.updateTripInfo(userTripInfoDto);
			return ResponseEntity.ok("");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// ### saved places ###

	@PostMapping("visited-place")
	public ResponseEntity<?> saveVisitedPlace(@RequestBody UserVisitedPlaceDto userVisitedPlaceDto) {
		try {
			userTripInfoService.saveVisitedPlace(userVisitedPlaceDto);
			return ResponseEntity.ok("");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("visited-place")
	public ResponseEntity<?> updateVisitedPlace(@RequestBody UserVisitedPlaceDto userVisitedPlaceDto) {
		try {
			userTripInfoService.updateVisitedPlace(userVisitedPlaceDto);
			return ResponseEntity.ok("");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("visited-place")
	public ResponseEntity<?> deleteVisitedPlace(Long userVisitedPlaceId) {
		try {
			userTripInfoService.deleteVisitedPlace(userVisitedPlaceId);
			return ResponseEntity.ok("");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// ### saved places ##

	@PostMapping("saved-place")
	public ResponseEntity<?> savePlaceToMyTripInfo(@RequestBody UserSavedPlaceDto userSavedPlaceDto) {
		try {
			userTripInfoService.savePlaceToMyTripInfo(userSavedPlaceDto);
			return ResponseEntity.ok("");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("saved-place")
	public ResponseEntity<?> updatePlaceToMyTripInfo(@RequestBody UserSavedPlaceDto userSavedPlaceDto) {
		try {
			userTripInfoService.updatePlaceToMyTripInfo(userSavedPlaceDto);
			return ResponseEntity.ok("");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("saved-place")
	public ResponseEntity<?> deletePlaceFromMyTripInfo(Long userSavedPlaceId) {
		try {
			userTripInfoService.deletePlaceFromMyTripInfo(userSavedPlaceId);
			return ResponseEntity.ok("");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// ### saved courses ###

	@PostMapping("saved-course")
	public ResponseEntity<?> saveCourseToMyTripInfo(@RequestBody UserSavedCourseDto userSavedCourseDto) {
		try {
			userTripInfoService.saveCourseToMyTripInfo(userSavedCourseDto);
			return ResponseEntity.ok("");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("saved-course")
	public ResponseEntity<?> updateCourseToMyTripInfo(@RequestBody UserSavedCourseDto userSavedCourseDto) {
		try {
			userTripInfoService.updateCourseToMyTripInfo(userSavedCourseDto);
			return ResponseEntity.ok("");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("saved-course")
	public ResponseEntity<?> deleteCourseFromMyTripInfo(Long userSavedCourseId) {
		try {
			userTripInfoService.deleteCourseFromMyTripInfo(userSavedCourseId);
			return ResponseEntity.ok("");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}

}
