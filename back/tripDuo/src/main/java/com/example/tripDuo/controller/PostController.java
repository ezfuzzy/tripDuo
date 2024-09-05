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

import com.example.tripDuo.dto.PostDto;
import com.example.tripDuo.service.PostService;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
	
	@Autowired
	private PostService service;
	
	@GetMapping("/{type}")
	public ResponseEntity<List<PostDto>> getPostList(@PathVariable String type) {
		return ResponseEntity.ok(service.getPostList(type));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PostDto> getPost(@PathVariable("id") Long id) {
		return ResponseEntity.ok(service.getPostById(id));
	}
	
	@PostMapping("/{type}")
	public ResponseEntity<String> writePost(@PathVariable("type") String type, @RequestBody PostDto dto){
		System.out.println(dto.toString());
		dto.setType(type);
		service.writePost(dto);
		return ResponseEntity.ok(dto.toString());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PostDto> editPost(@PathVariable("id") Long id, @RequestBody PostDto dto){
		return ResponseEntity.ok(service.updatePost(dto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(@PathVariable("id") Long id){
		service.deletePost(id);
		return ResponseEntity.ok("deleted");
	}
	
}
