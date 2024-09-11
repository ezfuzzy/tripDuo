package com.example.tripDuo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.Post;
import com.example.tripDuo.enums.PostType;

public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findByTypeOrderByIdDesc(PostType type);
}
