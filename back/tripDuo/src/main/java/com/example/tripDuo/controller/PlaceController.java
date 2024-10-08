package com.example.tripDuo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@GetMapping("/{id:[0-9]+}")
	public ResponseEntity<PlaceDto> getPost(@PathVariable("id") Long id) {
		return ResponseEntity.ok(placeService.getPlaceById(id));
	}
	
	@PostMapping
	public ResponseEntity<String> writePost(@RequestBody PlaceDto dto){
		placeService.addPlace(dto);
		return ResponseEntity.ok(dto.toString());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PlaceDto> editPost(@PathVariable("id") Long id, @RequestBody PlaceDto dto){
		return ResponseEntity.ok(placeService.updatePlace(dto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(@PathVariable("id") Long id){
		placeService.deletePlace(id);
		return ResponseEntity.ok("deleted");
	}
}
