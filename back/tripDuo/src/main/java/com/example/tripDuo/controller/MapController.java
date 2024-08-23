package com.example.tripDuo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/map")
public class MapController {
	@GetMapping("googleKey")
	public String googleKey() {
		return googleKey;
	}
	
	@GetMapping("kakaoKey")
	public String kakaoKey() {
		return kakaoKey;
	}
	
	@Value("${googleMap.key}")
	private String googleKey;
	@Value("${kakaoMap.key}")
	private String kakaoKey; 
}