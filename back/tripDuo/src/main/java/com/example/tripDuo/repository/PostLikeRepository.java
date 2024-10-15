package com.example.tripDuo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>{
	long countByPostId(Long postId);
	void deleteByPostIdAndUserId(Long postId, Long userId);
	List<PostLike> findByUserId(Long userId);
	boolean existsByPostIdAndUserId(Long postId, Long userId); // 같은 게시글에 2개 이상의 좋아요 남기지 못하게 
}
