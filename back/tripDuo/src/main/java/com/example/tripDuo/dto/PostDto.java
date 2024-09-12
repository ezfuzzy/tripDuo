package com.example.tripDuo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.tripDuo.entity.Post;
import com.example.tripDuo.enums.PostStatus;
import com.example.tripDuo.enums.PostType;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostDto {
    private Long id;
    
    private Long userId;
    private String writer;
    private PostType type; // mate / course / 여행기 / 커뮤니티
    private String title;
    
    private String content; // 메이트, 커뮤니티 게시글에만 있음
    private JsonNode postData; // 코스, 여행기 게시글에만 있음
    
    private String country;
    private String city;
    
    private String[] tags;
    
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private Float rating;
    private PostStatus status; // mate 모집(구인)중, 모집완료, 삭제됨 등
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    
    
    // ### for app ###
    
    private boolean isLike;
    private List<PostCommentDto> commentList;
    
    private int startRowNum, endRowNum;
    private int pageNum = 1;

    private String condition = "";
    private String keyword = "";

    private int prevNum, nextNum;
    
    
    // ### toDto ###
    
    public static PostDto toDto(Post entity) {
        return PostDto.builder()
                .id(entity.getId())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .writer(entity.getWriter())
                .type(entity.getType())
                .title(entity.getTitle())
                .content(entity.getContent())
                .postData(entity.getPostData())
                .country(entity.getCountry())
                .city(entity.getCity())
                .tags(entity.getTags())
                .viewCount(entity.getViewCount())
                .likeCount(entity.getLikeCount())
                .commentCount(entity.getCommentCount())
                .rating(entity.getRating())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
    
}