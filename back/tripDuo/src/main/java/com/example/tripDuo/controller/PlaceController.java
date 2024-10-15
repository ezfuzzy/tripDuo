package com.example.tripDuo.controller;

import java.util.List;

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
import com.example.tripDuo.service.PlaceService;

@RestController
@RequestMapping("/api/v1/places")
public class PlaceController {

	private final PlaceService placeService;

	public PlaceController(PlaceService placeService) {
		this.placeService = placeService;
	}
	
	@GetMapping
	public ResponseEntity<List<PlaceDto>> getPlaceList() {
		return ResponseEntity.ok(placeService.getPlaceList());
	}
	
	@GetMapping("/{postId:[0-9]+}")
	public ResponseEntity<PlaceDto> getPlace(@PathVariable("postId") Long postId) {
		return ResponseEntity.ok(placeService.getPlaceById(postId));
	}
	
	@PostMapping
	public ResponseEntity<String> addPlace(@RequestBody PlaceDto postDto){
		placeService.addPlace(postDto);
		return ResponseEntity.ok(postDto.toString());
	}
	
	@PutMapping("/{postId}")
	public ResponseEntity<PlaceDto> updatePlace(@PathVariable("postId") Long postId, @RequestBody PlaceDto postDto){
		return ResponseEntity.ok(placeService.updatePlace(postDto));
	}
	
	@DeleteMapping("/{postId}")
	public ResponseEntity<String> deletePost(@PathVariable("postId") Long postId){
		placeService.deletePlace(postId);
		return ResponseEntity.ok("deleted");
	}
}
