package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.tripDuo.entity.PostRating;

public interface PostRatingRepository extends JpaRepository<PostRating, Long>{
	
    @Query("SELECT AVG(r.rating) FROM PostRating r WHERE r.post.id = :postId")
    Float findAverageRatingByPostId(@Param("postId") Long postId);
    
	long countByPostIdAndUserId(Long postId, Long userId); // 같은 게시글에 2개 이상의 평점 남기지 못하게 
}
