package com.example.tripDuo.dto;

import java.time.LocalDateTime;
import java.util.List;

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
	private String type; // mate / course / 여행기 / 커뮤니티
	
    private String title;
    private String content; // 메이트, 커뮤니티 게시글
    private String country; 
    private String city;
    private List<String> tags;
    private List<Day> days;

    private Long viewCount;
    private Long likeCount;
    private Float rating;
    
    private String status; // mate 모집(구인)중, 모집완료, 삭제됨 등
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    
    @Data
    public static class Day {
        private List<Place> places;
        private String dayMemo;
    }

    @Data
    public static class Place {
		private String addressName;
		private String categoryGroupCode;
		private String categoryGroupName;
		private String categoryName;
		private String id;
		private String phone;
		private String placeName;
		private String placeUrl;
		private String roadAddressName;
		private Position position;
		private int dayIndex;
		private int placeIndex;
		private String placeMemo;
    }

    @Data
    public static class Position {
        private double latitude;
        private double longitude;
    }
}