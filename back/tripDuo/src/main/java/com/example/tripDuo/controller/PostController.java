package com.example.tripDuo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tripDuo.dto.PostDto;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
	
//	@Autowired
//	private PostService service;
	
	@GetMapping
	public ResponseEntity<PostDto> getList() {
		return null;
	}
	
	@PostMapping
	public ResponseEntity<String> writePost(@RequestBody PostDto postData){
		System.out.println(postData.toString());
		
		return ResponseEntity.ok("posting success");
	}
	
	
}
