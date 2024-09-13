package com.example.tripDuo.controller;

import java.util.List;
import java.util.Map;

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
	
	@GetMapping("/{postType:[a-zA-Z]+}")
	public ResponseEntity<List<PostDto>> getPostList(@PathVariable("postType") String type, PostDto dto) {
		
		PostType postType;
		
        try {
            postType = PostType.fromString(type);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
		return ResponseEntity.ok(postService.getPostList(postType));
	}	
	
	@GetMapping("/{postId:[0-9]+}")
	public ResponseEntity<Map<String, Object>> getPostDetailById(PostDto dto) {
		// 글 자세히 보기 페이지에서 axios할 api end point 
		try {
			return ResponseEntity.ok(postService.getPostDetailById(dto));
		} catch (EntityNotFoundException e) {
	        // 게시글이 존재하지 않는 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    } catch (Exception e) {
	        // 기타 예외에 대한 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    } 
	}
	
	@GetMapping("/{postId:[0-9]+}/update")
	public ResponseEntity<PostDto> getPostById(@PathVariable("postId") Long postId) {
		// 글 수정 페이지에서 axios
		try {
			return ResponseEntity.ok(postService.getPostById(postId));
		} catch (EntityNotFoundException e) {
	        // 게시글이 존재하지 않는 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    } catch (Exception e) {
	        // 기타 예외에 대한 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    } 
	}
	
	@PostMapping("/{postType:[a-zA-Z]+}")
	public ResponseEntity<String> writePost(@PathVariable("postType") String type, @RequestBody PostDto dto){
		
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
	
	@PutMapping("/{postId:[0-9]+}")
	public ResponseEntity<String> updatePost(@RequestBody PostDto dto){
		
		try {
			postService.updatePost(dto);
			return ResponseEntity.ok("Post is updated successfully");
		} catch (EntityNotFoundException e) {
	        // 게시글이 존재하지 않는 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (Exception e) {
	        // 기타 예외에 대한 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
	    } 
	}
	
	@DeleteMapping("/{postId:[0-9]+}")
	public ResponseEntity<String> deletePost(@PathVariable("postId") Long postId){
		
		try {
			postService.deletePost(postId);
			return ResponseEntity.ok("Post is deleted successfully");
		} catch (Exception e) {
	        // 기타 예외에 대한 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
	    } 
	}
	
	// ### comment ###	
	
	@PostMapping("/{postId:[0-9]+}/comments")
	public ResponseEntity<String> writeComment(@RequestBody PostCommentDto dto) {
		
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
	
	@GetMapping("/{postId:[0-9]+}/comments")
	public Map<String, Object> getCommentList(@PathVariable("num") Long postId, int pageNum) {
		PostCommentDto dto = new PostCommentDto();
		dto.setPostId(postId);
		dto.setPageNum(pageNum);
    
		return postService.getCommentList(dto);
	}
	
	
	
	@PutMapping("/{postId:[0-9]+}/comments/{commentId:[0-9]+}")
	public ResponseEntity<String> updateComment(@RequestBody PostCommentDto dto) {
		
		try {
			postService.updateComment(dto);
			return ResponseEntity.ok("Comment is updated successfully");
		} catch (EntityNotFoundException e) {
	        // 게시글이 존재하지 않는 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (Exception e) {
	        // 기타 예외에 대한 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
	    } 
	}
	
	@DeleteMapping("/{postId:[0-9]+}/comments/{commentId:[0-9]+}")
	public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId) {
		
		try {
			postService.deleteComment(commentId);
			return ResponseEntity.ok("Comment is deleted successfully");
		} catch (EntityNotFoundException e) {
	        // 게시글이 존재하지 않는 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (Exception e) {
	        // 기타 예외에 대한 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
	    } 
	}
	
	// ### like ###
	
	@PostMapping("/{postId:[0-9]+}/likes")
	public ResponseEntity<String> addLikeToPost(@RequestBody PostLikeDto dto) {

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
	
	@DeleteMapping("/{postId:[0-9]+}/likes/{likeId:[0-9]+}")
	public ResponseEntity<String> deleteLikeFromPost(@PathVariable("likeId") Long likeId) {

		try {
	        postService.deleteLikeFromPost(likeId);
	        return ResponseEntity.ok("Like is deleted successfully");
	    } catch (EntityNotFoundException e) {
	        // 게시글이 존재하지 않는 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (Exception e) {
	        // 기타 예외에 대한 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
	    } 
	}
	
	
	// ### rating ###
	
	@PostMapping("/{postId:[0-9]+}/ratings")
	public ResponseEntity<String> addRatingToPost(@RequestBody PostRatingDto dto) {
		
	    try {
			postService.addRatingToPost(dto);
	        return ResponseEntity.ok("Rating added successfully");
	    } catch (IllegalStateException e) {
	        // 이미 평점을 남긴 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    } catch (EntityNotFoundException e) {
	        // 게시글이 존재하지 않는 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (Exception e) {
	        // 기타 예외에 대한 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
	    }
	}

	@PutMapping("/{postId:[0-9]+}/ratings/{ratingId:[0-9]+}")
	public ResponseEntity<String> updateRatingForPost(@RequestBody PostRatingDto dto) {
		
	    try {
			postService.updateRatingForPost(dto);
	        return ResponseEntity.ok("Rating is updated successfully");
	    } catch (IllegalStateException e) {
	        // 남긴 퍙잠이 없는 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    } catch (EntityNotFoundException e) {
	        // 게시글이 존재하지 않는 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (Exception e) {
	        // 기타 예외에 대한 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
	    }
	}
	
	@DeleteMapping("/{postId:[0-9]+}/ratings/{ratingId:[0-9]+}")
	public ResponseEntity<String> deleteRatingFromPost(@PathVariable("ratingId") Long ratingId) {
		
	    try {
			postService.deleteRatingFromPost(ratingId);
	        return ResponseEntity.ok("Rating is deleted successfully");
	    } catch (EntityNotFoundException e) {
	        // 게시글이 존재하지 않는 경우에 대한 처리
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (Exception e) {
	        // 기타 예외에 대한 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
	    }
	}
	
}
