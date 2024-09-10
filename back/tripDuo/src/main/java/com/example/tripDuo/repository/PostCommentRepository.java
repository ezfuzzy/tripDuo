package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.PostComment;

public interface PostCommentRepository extends JpaRepository<PostComment, Long>{

}
