package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
