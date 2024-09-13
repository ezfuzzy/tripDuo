package com.example.tripDuo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.PostComment;

public interface PostCommentRepository extends JpaRepository<PostComment, Long>{
    long countByPostId(Long postId);
    Page<PostComment> findByPostIdOrderByParentCommentIdAscCreatedAtAsc(Long postId, Pageable pageable);
}
