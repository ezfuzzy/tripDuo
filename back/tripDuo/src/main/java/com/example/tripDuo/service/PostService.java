package com.example.tripDuo.service;

import java.util.List;

import com.example.tripDuo.dto.PostCommentDto;
import com.example.tripDuo.dto.PostDto;
import com.example.tripDuo.dto.PostLikeDto;
import com.example.tripDuo.dto.PostRatingDto;
import com.example.tripDuo.enums.PostType;

public interface PostService {
	
		// ### post ### 
		
		public List<PostDto> getPostList(PostType type); // 기본 getList
		// getPostList - 페이징 + 검색
		public PostDto getPostById(Long postId); // 간단한 
//		public PostDto getPostByUsername(String username);
		public PostDto getPostDetailById(Long postId);
		
		// getPostDetailById - 페이징 + 검색 ... 자세한 정보 + 댓글 
		
		public void writePost(PostDto dto);
		public PostDto updatePost(PostDto dto);
		public void deletePost(Long postId);
		
		// ### comment ###
		
		public void writeComment(PostCommentDto dto);
		public void updateComment(PostCommentDto dto);
		public void deleteComment(Long commentId);
		
		// ### like ###
		public void addLikeToPost(PostLikeDto dto);
		public void deleteLikeFromPost(Long likeId);
		
		// ### rating ###
		public void addRatingToPost(PostRatingDto dto);
		public void updateRatingForPost(PostRatingDto dto);
		public void deleteRatingFromPost(Long ratingId);
		
}
