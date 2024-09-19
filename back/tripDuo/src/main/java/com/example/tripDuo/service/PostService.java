package com.example.tripDuo.service;

import java.util.Map;

import com.example.tripDuo.dto.PostCommentDto;
import com.example.tripDuo.dto.PostDto;
import com.example.tripDuo.dto.PostLikeDto;
import com.example.tripDuo.dto.PostRatingDto;

public interface PostService {
	
	// ### post ### 

	public void writePost(PostDto dto);
		
	public Map<String, Object> getPostList(PostDto postDto); // 기본 getList
	// getPostList - 페이징 + 검색
	public PostDto getPostById(Long postId); // 간단한 
//		public PostDto getPostByUsername(String username);

	// getPostDetailById - 페이징 + 검색 ... 자세한 정보 + 댓글 
	public Map<String, Object> getPostDetailById(PostDto dto);
	
	public void updatePost(PostDto dto);
	public void deletePost(Long postId);
	
	/*
	 * like, rating은 
	 * write, delete 할때마다 post의 각 count와 rating을 업데이트 하므로 
	 * get 요청은 따로 없음
	 * -> 추후 내 comment, like, rating을 조회할 수 있도록 구현할 예정 
	 */
	
	// ### comment ###		
 
	public void writeComment(PostCommentDto dto);

	public Map<String, Object> getCommentList(PostCommentDto dto);
	
	public void updateComment(PostCommentDto dto);
	public void deleteComment(Long commentId);
	
	// ### like ###
	
	public void addLikeToPost(PostLikeDto dto);
	// get
	public void deleteLikeFromPost(Long likeId);
	
	// ### rating ###
	public void addRatingToPost(PostRatingDto dto);
	// get
	public void updateRatingForPost(PostRatingDto dto);
	public void deleteRatingFromPost(Long ratingId);
		
}
