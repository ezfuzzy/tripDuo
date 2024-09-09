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
//	@GetMapping("/{type:[a-zA-Z]+}/{id}")
//	public ResponseEntity<String>
	
	
	// ### rating ###
	
	
}
