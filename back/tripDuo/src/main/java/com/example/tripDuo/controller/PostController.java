package com.example.tripDuo.controller;

import java.util.List;

import com.example.tripDuo.dto.PostLikeDto;
import com.example.tripDuo.dto.PostRatingDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tripDuo.dto.PostDto;
import com.example.tripDuo.service.PostService;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	@GetMapping("/{type:[a-zA-Z]+}")
	public ResponseEntity<List<PostDto>> getPostList(@PathVariable("type") String type) {
		return ResponseEntity.ok(postService.getPostList(type));
	}
	
	@GetMapping("/{id:[0-9]+}")
	public ResponseEntity<PostDto> getPost(@PathVariable("id") Long id) {
		return ResponseEntity.ok(postService.getPostById(id));
	}
	
	@PostMapping("/{type}")
	public ResponseEntity<String> writePost(@PathVariable("type") String type, @RequestBody PostDto dto){
		System.out.println(dto.toString());
		dto.setType(type);
		postService.writePost(dto);
		return ResponseEntity.ok(dto.toString());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PostDto> editPost(@PathVariable("id") Long id, @RequestBody PostDto dto){
		return ResponseEntity.ok(postService.updatePost(dto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(@PathVariable("id") Long id){
		postService.deletePost(id);
		return ResponseEntity.ok("deleted");
	}
	
	// ### comment ###
	
	
	// ### like ###
	@PostMapping("/{type:[a-zA-Z]+}/{id}/likes")
	public ResponseEntity<String> addLikeToPost(@PathVariable("type") String type, @PathVariable("id") Long id, @RequestBody PostLikeDto dto) {

		return null;
	}
	
	
	// ### rating ###
	@PostMapping("/{type:[a-zA-Z]+}/{id}/ratings")
	public ResponseEntity<String> addRatingToPost(@PathVariable("type") String type, @PathVariable("id") Long id, @RequestBody PostRatingDto dto) {

		return null;
	}

	
}
