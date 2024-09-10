package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.PostLikeDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name="post_likes")
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long postId;
    private Long userId;

    private LocalDateTime createdAt;

    @PrePersist
    public void onPrePersist() {
        createdAt = LocalDateTime.now();
    }
    
    public static PostLike toEntity(PostLikeDto dto){
        return PostLike.builder()
            .id(dto.getId())
            .postId(dto.getPostId())
            .userId(dto.getUserId())
            .createdAt(dto.getCreatedAt())
            .build();
    }
}
