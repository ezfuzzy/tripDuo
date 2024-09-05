package com.example.tripDuo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	public List<Post> findByType(String type);
}
