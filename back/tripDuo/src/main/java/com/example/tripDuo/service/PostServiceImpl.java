package com.example.tripDuo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tripDuo.dto.PostCommentDto;
import com.example.tripDuo.dto.PostDto;
import com.example.tripDuo.dto.PostLikeDto;
import com.example.tripDuo.dto.PostRatingDto;
import com.example.tripDuo.dto.UserProfileInfoDto;
import com.example.tripDuo.entity.Post;
import com.example.tripDuo.entity.PostComment;
import com.example.tripDuo.entity.PostLike;
import com.example.tripDuo.entity.PostRating;
import com.example.tripDuo.entity.UserProfileInfo;
import com.example.tripDuo.enums.PostType;
import com.example.tripDuo.repository.PostCommentRepository;
import com.example.tripDuo.repository.PostLikeRepository;
import com.example.tripDuo.repository.PostRatingRepository;
import com.example.tripDuo.repository.PostRepository;
import com.example.tripDuo.repository.UserProfileInfoRepository;
import com.example.tripDuo.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostServiceImpl implements PostService {

	@Value("${cloud.aws.cloudfront.url}")
	private String cloudFrontUrl;
	
	private final PostRepository postRepo;
	private final PostCommentRepository postCommentRepo;
	private final PostLikeRepository postLikeRepo;
	private final PostRatingRepository postRatingRepo;
	
	private final UserRepository userRepo;
	private final UserProfileInfoRepository userProfileInfoRepo;

	final int POST_PAGE_SIZE = 10;
	final int COMMENT_PAGE_SIZE = 10;
	
	public PostServiceImpl(PostRepository postRepo, PostCommentRepository postCommentRepo, 
			PostLikeRepository postLikeRepo, PostRatingRepository postRatingRepo, 
			UserRepository userRepo, UserProfileInfoRepository userProfileInfoRepo) {
		this.postRepo = postRepo;
		this.postCommentRepo = postCommentRepo;
		this.postLikeRepo = postLikeRepo;
		this.postRatingRepo = postRatingRepo;
		this.userRepo = userRepo;
		this.userProfileInfoRepo = userProfileInfoRepo;
	}
	
	/**
	 * @date : 2024. 9. 13.
	 * @user : 김민준
	 * getPostList: post list 리턴
	 * 
	 * @param postType
	 * @return
	 * TODO : pageable과 condition 적용(검색)
	 */
	@Override
	public List<PostDto> getPostList(PostType postType) {
		// type별 get list 
		return postRepo.findByTypeOrderByIdDesc(postType).stream().map(PostDto::toDto).toList();
	}

	/**
	 * @date : 2024. 9. 14.
	 * @user : 김민준
	 * getPostById : post 수정 페이지에서 호출할 api. 기본 정보만 return
	 *
	 * @param postId
	 * @return
	 */
	@Override
	public PostDto getPostById(Long postId) {
		// 이건 수정폼에서 쓸 메소드
		return PostDto.toDto(postRepo.findById(postId)
		        .orElseThrow(() -> new EntityNotFoundException("Post not found")));
	}

	/**
	 * @date : 2024. 9. 14.
	 * @user : 김민준
	 * getPostDetailById : post 상세보기 페이지에서 호출할 api
	 * 					   condition과 keyword 받은 다음에 다시 return
	 *					   댓글 첫번째 페이지와 함께 리턴.
	 *					   viewCount 업데이트 - 추후 수정
	 *					   무한 스크롤을 위한 totalPageCount return
	 * @param postDto
	 * @return
	 * TODO : viewCount 개선
	 */
	@Override
	public Map<String, Object> getPostDetailById(PostDto postDto) {
		// 글 자세히 보기 페이지에서 필요한 정보 return 
		Post post = postRepo.findById(postDto.getId())
				.orElseThrow(() -> new EntityNotFoundException("Post not found"));
		
		PostDto existingDto = PostDto.toDto(post);
		
		existingDto.setCondition(postDto.getCondition());
		existingDto.setKeyword(postDto.getKeyword());

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		if(username != null && !username.equals("anonymousUser")) {
			existingDto.setLike(postLikeRepo.existsByPostIdAndUserId(existingDto.getId(), userRepo.findByUsername(username).getId()));
		}
		
		// 댓글 list 
		Sort sort = Sort.by("parentCommentId").ascending().and(Sort.by("createdAt").ascending());
		Pageable pageable = PageRequest.of(0, COMMENT_PAGE_SIZE, sort);
		Page<PostComment> page = postCommentRepo.findByPostIdOrderByParentCommentIdAscCreatedAtAsc(postDto.getId(), pageable);
		List<PostCommentDto> commentList = page.stream().map(PostCommentDto::toDto).toList();
		
		int totalCommentPages = (int) (existingDto.getCommentCount() / COMMENT_PAGE_SIZE);
		
		// view count + 1
		
		UserProfileInfoDto upiDto = UserProfileInfoDto.toDto(post.getUserProfileInfo(), cloudFrontUrl);
		
		existingDto.setViewCount(existingDto.getViewCount()+1);
		postRepo.save(Post.toEntity(existingDto, userProfileInfoRepo.findById(existingDto.getUserId()).get()));
		
		return Map.of(
			"dto", existingDto, 
			"userProfileInfo", upiDto,
			"commentList", commentList, 
			"totalCommentPages", totalCommentPages
		);
	}

	/**
	 * @date : 2024. 9. 14.
	 * @user : 김민준
	 * writePost : post 작성, userProfileInfo와 함께 저장
	 *
	 * @param postDto
	 */
	@Override
	public void writePost(PostDto postDto) {
		UserProfileInfo userProfileInfo = userProfileInfoRepo.findById(postDto.getUserId()).get();
		postRepo.save(Post.toEntity(postDto, userProfileInfo));
	}

	/**
	 * @date : 2024. 9. 14.
	 * @user : 김민준
	 * updatePost : post 수정 메소드 - post의 모든 정보가 postDto에 담겨서 넘어옴
	 *
	 * @param postDto
	 */
	@Override
	public void updatePost(PostDto postDto) {
		
		UserProfileInfo userProfileInfo = userProfileInfoRepo.findById(postDto.getUserId())
				.orElseThrow(() -> new EntityNotFoundException("Post not found"));

		// put mapping이니까 정보 삭제 안되게 ... 
//		PostDto existingPost = PostDto.toDto(postRepo.findById(postDto.getId()).get());
		
		// 1. 만약 기존의 모든 정보가 그대로 넘어오면
		postRepo.save(Post.toEntity(postDto, userProfileInfo));
		
		// 2. 만약 기존 정보중 일부만 넘어오면 -> controller에서 setId하고 넘겨준 다음에 로직 실행
		// 협의하고 코드 짤 부분
	}

	/**
	 * @date : 2024. 9. 14.
	 * @user : 김민준
	 * deletePost : post 삭제
	 *
	 * @param postId
	 * TODO : soft 삭제 ?
	 */
	@Override
	public void deletePost(Long postId) {
		postRepo.deleteById(postId);
	}
	
	// ### comment ###
	


	
	/**
	 * @date : 2024. 9. 13.
	 * @user : 김민준
	 * writeComment: comment 추가 post의 commentCount 갱신
	 * 
	 * @param postCommentDto
	 * TODO : 댓글의 group_id, depth 설정 로직 생각 + 추가
	 */
	@Override
	@Transactional
	public void writeComment(PostCommentDto postCommentDto) {
		// 댓글의 parent comment id와 to username 은 front 에서 넘어옴
		Post existingPost = postRepo.findById(postCommentDto.getPostId())
	            .orElseThrow(() -> new EntityNotFoundException("Post not found"));
		UserProfileInfo userProfileInfo = userProfileInfoRepo.findByUserId(postCommentDto.getUserId());

		// 댓글 db 저장
		postCommentRepo.save(PostComment.toEntity(postCommentDto, userProfileInfo));
		
		// post의 댓글 수 update
		existingPost.setCommentCount(postCommentRepo.countByPostId(existingPost.getId()));
	}

	/**
	 * @date : 2024. 9. 14.
	 * @user : 김민준
	 * getCommentList : postCommentDto에 postId와 pageNum이 설정되어서 넘어옴
	 * 					pageNum에 해당하는 댓글 list return.
	 *
	 * @param postCommentDto
	 * @return
	 */
	@Override
	public Map<String, Object> getCommentList(PostCommentDto postCommentDto) {
		
	    
	    // 정렬 기준 설정
	    Sort sort = Sort.by("parentCommentId").ascending().and(Sort.by("createdAt").ascending());
	    Pageable pageable = PageRequest.of(postCommentDto.getPageNum() - 1, COMMENT_PAGE_SIZE, sort);

	    // 댓글 페이지 조회
	    Page<PostComment> page = postCommentRepo.findByPostIdOrderByParentCommentIdAscCreatedAtAsc(postCommentDto.getPostId(), pageable);
	    List<PostCommentDto> commentList = page.stream().map(PostCommentDto::toDto).toList();
	    
	    // 총 페이지 수 계산
	    int totalCommentPages = page.getTotalPages();

	    // 결과 반환
	    return Map.of(
	        "commentList", commentList,
	        "totalCommentPages", totalCommentPages
	    );
	}
	
	/**
	 * @date : 2024. 9. 13.
	 * @user : 김민준
	 * updateComment: comment 수정
	 * 
	 * @param postCommentDto
	 */
	@Override
	@Transactional
	public void updateComment(PostCommentDto postCommentDto) {
		
		UserProfileInfo userProfileInfo = userProfileInfoRepo.findByUserId(postCommentDto.getUserId());
		// 댓글 db 저장
		postCommentRepo.save(PostComment.toEntity(postCommentDto, userProfileInfo));
		
	}
	
	/**
	 * @date : 2024. 9. 13.
	 * @user : 김민준
	 * deleteComment: comment 삭제 + post의 commentCount 갱신 
	 * 
	 * @param commentId
	 * TODO : 실제로 삭제하는 것이 아닌 deletedAt 에 값을 설정
	 */
	@Override
	@Transactional
	public void deleteComment(Long commentId) {
		Post existingPost = postRepo.findById(postCommentRepo.findById(commentId).get().getPostId())
	            .orElseThrow(() -> new EntityNotFoundException("Post not found"));
		
		postCommentRepo.deleteById(commentId);
		existingPost.setCommentCount(postCommentRepo.countByPostId(existingPost.getId()));
	}
	
	// ### like ### 

	/**
	 * @date : 2024. 9. 13.
	 * @user : 김민준
	 * addLikeToPost: post에 대한 like정보 추가 + post의 likeCount 갱신
	 * 
	 * @param postLikeDto
	 */
	@Override
	@Transactional
	public void addLikeToPost(PostLikeDto postLikeDto) {
		
		// userId, postId 검사 - 같은 유저가 같은 게시글에 2개 이상 좋아요 남기지 못하게
	    if (postLikeRepo.existsByPostIdAndUserId(postLikeDto.getPostId(), postLikeDto.getUserId())) {
	        throw new IllegalStateException("이미 좋아요를 누른 게시글 입니다.");
	    }		
		
		Post existingPost = postRepo.findById(postLikeDto.getPostId())
	            .orElseThrow(() -> new EntityNotFoundException("Post not found"));
		
		// 좋아요 db 저장
		postLikeRepo.save(PostLike.toEntity(postLikeDto));
		
		// post 좋아요 수 update
		existingPost.setLikeCount(postLikeRepo.countByPostId(existingPost.getId()));
	}
	
	/**
	 * @date : 2024. 9. 13.
	 * @user : 김민준
	 * deleteLikeFromPost: post에 대한 like정보 삭제 + post의 likeCount 갱신 
	 * 
	 * @param likeId
	 */
	@Override
	@Transactional
	public void deleteLikeFromPost(Long likeId) {
		
		Post existingPost = postRepo.findById(postLikeRepo.findById(likeId).get().getPostId())
	            .orElseThrow(() -> new EntityNotFoundException("Post not found"));
		
		postLikeRepo.deleteById(likeId);
		existingPost.setLikeCount(postLikeRepo.countByPostId(existingPost.getId()));
	}


	// ### rating ###
	
	/**
	 * @date : 2024. 9. 13.
	 * @user : 김민준
	 * addRatingToPost: post에 대한 rating정보 추가 + post의 rating 갱신 
	 * 
	 * @param postRatingDto
	 */
	@Override
	@Transactional
	public void addRatingToPost(PostRatingDto postRatingDto) {
		
		// userId, postId 검사 - 같은 유저가 같은 게시글에 2개 이상 평점 남기지 못하게
	    if (postRatingRepo.existsByPostIdAndUserId(postRatingDto.getPostId(), postRatingDto.getUserId())) {
	        throw new IllegalStateException("이미 평점을 남긴 게시글 입니다.");
	    }
	    
		Post existingPost = postRepo.findById(postRatingDto.getPostId())
	            .orElseThrow(() -> new EntityNotFoundException("Post not found"));
		
		// 평점 db 저장
		postRatingRepo.save(PostRating.toEntity(postRatingDto, existingPost));
		
		// post 평점 update
		existingPost.updateRating(postRatingRepo.findAverageRatingByPostId(existingPost.getId()));
	}

	/**
	 * @date : 2024. 9. 13.
	 * @user : 김민준
	 * updateRatingForPost: post에 대한 rating정보를 update - rating 값이 바뀔 수 있으니 post테이블의 rating 갱신
	 * 
	 * @param postRatingDto
	 */
	@Override
	@Transactional
	public void updateRatingForPost(PostRatingDto postRatingDto) {
		
		if (!postRatingRepo.existsByPostIdAndUserId(postRatingDto.getPostId(), postRatingDto.getUserId())) {
	        throw new IllegalStateException("게시글에 남긴 평점이 없습니다.");
	    }
	    
		Post existingPost = postRepo.findById(postRatingDto.getPostId())
	            .orElseThrow(() -> new EntityNotFoundException("Post not found"));
		
		// 평점 db 저장
		postRatingRepo.save(PostRating.toEntity(postRatingDto, existingPost));
		
		// post 평점 update
		existingPost.updateRating(postRatingRepo.findAverageRatingByPostId(existingPost.getId()));
	}
	
	/**
	 * @date : 2024. 9. 13.
	 * @user : 김민준
	 * deleteRatingFromPost: post에 대한 rating정보를 삭제 + post의 rating 갱신
	 * 
	 * @param ratingId
	 */
	@Override
	@Transactional
	public void deleteRatingFromPost(Long ratingId) {

		Post existingPost = postRatingRepo.findById(ratingId).get().getPost();
		
		postRatingRepo.deleteById(ratingId);
		
		// post 평점 update
		existingPost.updateRating(postRatingRepo.findAverageRatingByPostId(existingPost.getId()));
	}
	
}
