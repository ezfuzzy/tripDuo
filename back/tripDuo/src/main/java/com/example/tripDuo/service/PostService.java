package com.example.tripDuo.service;

import java.util.List;

import com.example.tripDuo.dto.PostDto;

public interface PostService {
	
		// ### getPost ### 
		public List<PostDto> getPostList();
		public PostDto getPostById(Long id);
//		public PostDto getPostByPostname(String username);
		
		// ### new post ###
		public void writePost(PostDto dto);
		
		// ### updatePost ###
		public Boolean updatePost(PostDto dto);
		
		// ### deletePost ###
		public void deletePost(Long id);
}
