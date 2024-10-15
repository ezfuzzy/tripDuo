package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.PostLikeDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name="post_likes", indexes = {
		@Index(name = "idx_post_likes_post_user", columnList = "post_id, userId")
})
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    private long userId;

    private LocalDateTime createdAt;

    @PrePersist
    public void onPrePersist() {
        createdAt = LocalDateTime.now();
    }
    
    public static PostLike toEntity(PostLikeDto dto, Post post){
        return PostLike.builder()
            .id(dto.getId())
            .post(post)
            .userId(dto.getUserId() != null ? dto.getUserId() : 0L)
            .createdAt(dto.getCreatedAt())
            .build();
    }
}
