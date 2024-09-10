package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>{
	
}
