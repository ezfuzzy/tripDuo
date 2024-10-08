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

import com.example.tripDuo.dto.PlaceDto;
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
			return ResponseEntity.ok("trip info updated");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// ### saved places ###

	@PostMapping("/visited-places")
	public ResponseEntity<?> saveVisitedPlace(@RequestBody PlaceDto placeDto) {
		try {
			userTripInfoService.saveVisitedPlace(placeDto);
			return ResponseEntity.ok("visited place saved");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/visited-places")
	public ResponseEntity<?> getVisitedPlaceList(@PathVariable("userId") Long userId) {
		try {
			return ResponseEntity.ok(userTripInfoService.getVisitedPlaceList(userId));
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@PutMapping("/visited-places")
	public ResponseEntity<?> updateUserVisitedPlace(@RequestBody UserVisitedPlaceDto userVisitedPlaceDto) {
		try {
			userTripInfoService.updateUserVisitedPlace(userVisitedPlaceDto);
			return ResponseEntity.ok("visited place updated");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("/visited-places")
	public ResponseEntity<?> deleteVisitedPlace(Long userVisitedPlaceId) {
		try {
			userTripInfoService.deleteVisitedPlace(userVisitedPlaceId);
			return ResponseEntity.ok("visited place deleted");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// ### saved places ##

	@PostMapping("/saved-places")
	public ResponseEntity<?> savePlaceToMyTripInfo(@RequestBody PlaceDto placeDto) {
		try {
			userTripInfoService.savePlaceToMyTripInfo(placeDto);
			return ResponseEntity.ok("saved place saved");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@GetMapping("/saved-places")
	public ResponseEntity<?> getSavedPlaceList(@PathVariable("userId") Long userId) {
		try {
			return ResponseEntity.ok(userTripInfoService.getSavedPlaceList(userId));
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@PutMapping("/saved-places")
	public ResponseEntity<?> updateUserSavedPlace(@RequestBody UserSavedPlaceDto userSavedPlaceDto) {
		try {
			userTripInfoService.updateUserSavedPlace(userSavedPlaceDto);
			return ResponseEntity.ok("saved place updated");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("/saved-places")
	public ResponseEntity<?> deletePlaceFromMyTripInfo(Long userSavedPlaceId) {
		try {
			userTripInfoService.deletePlaceFromMyTripInfo(userSavedPlaceId);
			return ResponseEntity.ok("saved place deleted");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// ### saved courses ###

	@PostMapping("/saved-courses")
	public ResponseEntity<?> saveCourseToMyTripInfo(@RequestBody UserSavedCourseDto userSavedCourseDto) {
		try {
			userTripInfoService.saveCourseToMyTripInfo(userSavedCourseDto);
			return ResponseEntity.ok("saved course saved");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/saved-courses")
	public ResponseEntity<?> getSavedCourseList(@PathVariable("userId") Long userId) {
		try {
			return ResponseEntity.ok(userTripInfoService.getSavedCourseList(userId));
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@PutMapping("/saved-courses")
	public ResponseEntity<?> updateCourseToMyTripInfo(@RequestBody UserSavedCourseDto userSavedCourseDto) {
		try {
			userTripInfoService.updateCourseToMyTripInfo(userSavedCourseDto);
			return ResponseEntity.ok("aved course updated");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("/saved-courses")
	public ResponseEntity<?> deleteCourseFromMyTripInfo(Long userSavedCourseId) {
		try {
			userTripInfoService.deleteCourseFromMyTripInfo(userSavedCourseId);
			return ResponseEntity.ok("aved course deleted");
		} catch (Exception error) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}

}
