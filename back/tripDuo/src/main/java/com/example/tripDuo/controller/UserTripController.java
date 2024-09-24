package com.example.tripDuo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tripDuo.dto.UserTripInfoDto;

@RestController
@RequestMapping("/api/v1/users/{userId}/trips")
public class UserTripController {
	
	@PostMapping
	public ResponseEntity<?> addTripInfo(@RequestBody UserTripInfoDto userTripInfoDto)  {
		
		return null; 
	}
	
	@PutMapping
	public ResponseEntity<?> updateTripInfo(@RequestBody UserTripInfoDto userTripInfoDto) {
		
		return null;
	}
	
}
