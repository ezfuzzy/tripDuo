package com.example.tripDuo.service;

import java.util.List;

import com.example.tripDuo.dto.PostCommentDto;
import com.example.tripDuo.dto.PostDto;
import com.example.tripDuo.dto.PostLikeDto;
import com.example.tripDuo.dto.PostRatingDto;

public interface PostService {
	
		// ### getPost ### 
		
		public List<PostDto> getPostList(String type); // 기본 getList
		// getPostList - 페이징 + 검색
		public PostDto getPostById(Long id); // 간단한 
//		public PostDto getPostByUsername(String username);
		public PostDto getPostDetailById(Long id);
		
		// getPostDetailById - 페이징 + 검색 ... 자세한 정보 + 댓글 
		
		// ### new post ###
		public void writePost(PostDto dto);
		
		// ### updatePost ###
		public PostDto updatePost(PostDto dto);
		
		// ### deletePost ###
		public void deletePost(Long id);
		
		// ### comment ###
		public void writeComment(PostCommentDto dto);
		
		
		// ### like ###
		public void addLikeToPost(PostLikeDto dto);
		
		
		// ### rating ###
		public void addRatingToPost(PostRatingDto dto);
		
		
}
