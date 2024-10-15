package com.example.tripDuo.service;

import java.util.List;
import java.util.Map;

import com.example.tripDuo.dto.PostCommentDto;
import com.example.tripDuo.dto.PostDto;
import com.example.tripDuo.dto.PostLikeDto;
import com.example.tripDuo.dto.PostRatingDto;
import com.example.tripDuo.entity.PostLike;

public interface PostService {
	
	// ### post ### 

	public void writePost(PostDto dto);
		
	public Map<String, Object> getPostList(PostDto postDto); // 기본 getList
	public Map<String, Object> getPostListForHome();
	// getPostList - 페이징 + 검색
	public PostDto getPostById(Long postId); // 간단한 
//		public PostDto getPostByUsername(String username);

	// getPostDetailById - 페이징 + 검색 ... 자세한 정보 + 댓글 
	public Map<String, Object> getPostDetailById(PostDto postDto);
	
	// 여행 코스 계획은 기본적으로 PRIVATE으로 작성됨 -> 여행 코스 게시판에 post할때도 update 메소드 사용
	public void updatePost(PostDto postDto);
	

	
	public void deletePost(Long postId);
	
	/*
	 * like, rating은 
	 * write, delete 할때마다 post의 각 count와 rating을 업데이트 
	 * get 요청은 모아보기 
	 */
	
	// ### comment ###		
 
	public PostCommentDto writeComment(PostCommentDto postCommentDto);

	public Map<String, Object> getCommentList(PostCommentDto postCommentDto);
	
	public void updateComment(PostCommentDto postCommentDto);
	public void deleteComment(Long commentId);
	
	// ### like ###
	
	public void addLikeToPost(PostLikeDto postLikeDto);
	public List<PostLike> getLikedPostList(Long userId);
	public void deleteLikeFromPost(Long postId, Long usreId);
	
	// ### rating ###
	public void addRatingToPost(PostRatingDto postRatingDto);
	// get
	public void updateRatingForPost(PostRatingDto postRatingDto);
	public void deleteRatingFromPost(Long ratingId);
		
}
