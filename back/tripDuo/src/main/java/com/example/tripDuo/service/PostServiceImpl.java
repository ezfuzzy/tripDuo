package com.example.tripDuo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tripDuo.dto.PostCommentDto;
import com.example.tripDuo.dto.PostDto;
import com.example.tripDuo.dto.PostLikeDto;
import com.example.tripDuo.dto.PostRatingDto;
import com.example.tripDuo.entity.Post;
import com.example.tripDuo.entity.PostComment;
import com.example.tripDuo.entity.PostLike;
import com.example.tripDuo.entity.PostRating;
import com.example.tripDuo.enums.PostType;
import com.example.tripDuo.repository.PostCommentRepository;
import com.example.tripDuo.repository.PostLikeRepository;
import com.example.tripDuo.repository.PostRatingRepository;
import com.example.tripDuo.repository.PostRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostServiceImpl implements PostService {

	private final PostRepository postRepo;
	private final PostCommentRepository postCommentRepo;
	private final PostLikeRepository postLikeRepo;
	private final PostRatingRepository postRatingRepo;
	
	public PostServiceImpl(PostRepository postRepo, PostCommentRepository postCommentRepo, PostLikeRepository postLikeRepo, PostRatingRepository postRatingRepo) {
		this.postRepo = postRepo;
		this.postCommentRepo = postCommentRepo;
		this.postLikeRepo = postLikeRepo;
		this.postRatingRepo = postRatingRepo;
	}
	
	@Override
	public List<PostDto> getPostList(PostType type) {
		// type별 get list 
		return postRepo.findByTypeOrderByIdDesc(type).stream().map(PostDto::toDto).toList();
	}

	@Override
	public PostDto getPostById(Long id) {
		// 이건 수정폼에서 쓸 메소드
		return PostDto.toDto(postRepo.findById(id)
		        .orElseThrow(() -> new EntityNotFoundException("Post not found")));
	}

	@Override
	public PostDto getPostDetailById(Long id) {
		
		PostDto dto = PostDto.toDto(postRepo.findById(id).get());
		// 각종 처리 
		// + 댓글 목록도 가져와야할듯 
		return null;
	}
	
	
	@Override
	public void writePost(PostDto dto) {
		postRepo.save(Post.toEntity(dto));
	}
	
	@Override
	public PostDto updatePost(PostDto dto) {
		
		// put mapping이니까 정보 삭제 안되게 ... 
//		PostDto existingPost = PostDto.toDto(postRepo.findById(dto.getId()).get());
		
		// 1. 만약 기존의 모든 정보가 그대로 넘어오면
		postRepo.save(Post.toEntity(dto));
		
		// 2. 만약 기존 정보중 일부만 넘어오면 -> controller에서 setId하고 넘겨준 다음에 로직 실행
		// 협의하고 코드 짤 부분
		
		return dto; // 정보가 다 들어있는 dto 반환 - 안해줘도 되긴함
	}

	@Override
	public void deletePost(Long id) {
		postRepo.deleteById(id);
	}
	
	// ### comment ###
	
	@Override
	@Transactional
	public void writeComment(PostCommentDto dto) {
		
		// service code 
		// PostCommentDto를 return해줘야할수도있음 - 댓글 출력 ? 
		// groupId setting 
		
		Post existingPost = postRepo.findById(dto.getPostId())
	            .orElseThrow(() -> new EntityNotFoundException("Post not found"));
		
		// 댓글 db 저장
		postCommentRepo.save(PostComment.toEntity(dto));
		
		// post의 댓글 수 update
		existingPost.setCommentCount(postCommentRepo.countByPostId(existingPost.getId()));
	}
	
	// ### like ###

	@Override
	@Transactional
	public void addLikeToPost(PostLikeDto dto) {
		
		// userId, postId 검사 - 같은 유저가 같은 게시글에 2개 이상 좋아요 남기지 못하게
	    if (postLikeRepo.countByPostIdAndUserId(dto.getPostId(), dto.getUserId()) > 0) {
	        throw new IllegalStateException("이미 좋아요를 누른 게시글 입니다.");
	    }		
		
		Post existingPost = postRepo.findById(dto.getPostId())
	            .orElseThrow(() -> new EntityNotFoundException("Post not found"));
		
		// 좋아요 db 저장
		postLikeRepo.save(PostLike.toEntity(dto));
		
		// post 좋아요 수 update
		existingPost.setLikeCount(postLikeRepo.countByPostId(existingPost.getId()));
	}

	// ### rating ###
	
	@Override
	@Transactional
	public void addRatingToPost(PostRatingDto dto) {
		
		// userId, postId 검사 - 같은 유저가 같은 게시글에 2개 이상 평점 남기지 못하게
	    if (postRatingRepo.countByPostIdAndUserId(dto.getPostId(), dto.getUserId()) > 0) {
	        throw new IllegalStateException("이미 평점을 남긴 게시글 입니다.");
	    }
	    
		Post existingPost = postRepo.findById(dto.getPostId())
	            .orElseThrow(() -> new EntityNotFoundException("Post not found"));
		
		// 평점 db 저장
		postRatingRepo.save(PostRating.toEntity(dto));
		
		// post 평점 update
		existingPost.updateRating(postRatingRepo.findAverageRatingByPostId(existingPost.getId()));
	}

}
