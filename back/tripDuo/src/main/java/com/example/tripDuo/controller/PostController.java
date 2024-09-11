package com.example.tripDuo.controller;

import java.util.List;

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

import com.example.tripDuo.dto.PostCommentDto;
import com.example.tripDuo.dto.PostDto;
import com.example.tripDuo.dto.PostLikeDto;
import com.example.tripDuo.dto.PostRatingDto;
import com.example.tripDuo.enums.PostType;
import com.example.tripDuo.service.PostService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	@GetMapping("/{type:[a-zA-Z]+}")
	public ResponseEntity<List<PostDto>> getPostList(@PathVariable("type") String type, PostDto dto) {
		
		PostType postType;
		
        try {
            postType = PostType.fromString(type);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
		return ResponseEntity.ok(postService.getPostList(postType));
	}
	
	@GetMapping("/{id:[0-9]+}")
	public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long id) {

		try {
			return ResponseEntity.ok(postService.getPostById(id));
		} catch (EntityNotFoundException e) {
	        // 게시글이 존재하지 않는 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    } catch (Exception e) {
	        // 기타 예외에 대한 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    } 
	}
	
	@PostMapping("/{type}")
	public ResponseEntity<String> writePost(@PathVariable("type") String type, @RequestBody PostDto dto){
		
		PostType postType;
		
        try {
            postType = PostType.fromString(type);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid post type: " + type);
        }

        dto.setType(postType);
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
		return ResponseEntity.ok("Post id deleted");
	}
	
	// ### comment ###
	@PostMapping("/{type:[a-z]+}/{id}/comments")
	public ResponseEntity<String> writeComment(@PathVariable("id") Long id, @RequestBody PostCommentDto dto) {
		
		try {
			postService.writeComment(dto);
			return ResponseEntity.ok("Comment is written successfully");
		} catch (EntityNotFoundException e) {
	        // 게시글이 존재하지 않는 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (Exception e) {
	        // 기타 예외에 대한 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
	    } 
	}
	
	// ### like ###
	@PostMapping("/{type:[a-z]+}/{id}/likes")
	public ResponseEntity<String> addLikeToPost(@PathVariable("id") Long id, @RequestBody PostLikeDto dto) {

	    try {
	        postService.addLikeToPost(dto);
	        return ResponseEntity.ok("Like added successfully");
	    } catch (IllegalStateException e) {
	        // 이미 좋아요를 누른 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    } catch (EntityNotFoundException e) {
	        // 게시글이 존재하지 않는 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (Exception e) {
	        // 기타 예외에 대한 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
	    }
	}
	
	
	// ### rating ###
	@PostMapping("/{type:[a-zA-Z]+}/{id}/ratings")
	public ResponseEntity<String> addRatingToPost(@PathVariable("id") Long id, @RequestBody PostRatingDto dto) {
		
	    try {
			postService.addRatingToPost(dto);
	        return ResponseEntity.ok("Rating added successfully");
	    } catch (IllegalStateException e) {
	        // 이미 좋아요를 누른 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    } catch (EntityNotFoundException e) {
	        // 게시글이 존재하지 않는 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (Exception e) {
	        // 기타 예외에 대한 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
	    }
	}

	
}
