package com.example.tripDuo.dto;

import java.time.LocalDateTime;
import java.util.Map;

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
  private String title;
  private PostData data;  // JSON 데이터를 객체로 처리 -- 여행 코스랑 리뷰 게시글에만 저장됨 ( 나머진 null )
  private String content; // 메이트, 커뮤니티 게시글
  
  private Long user_id;
  private String type; // mate / course / 여행기 / 커뮤니티
  private String country;
  private String city;
  
  private String tags; // # 태그
  private Long views; // 조회수
  private Long likes; // 좋아요 수 - 코스, 여행기, 커뮤니티
  private Float rating; // 코스 
  private String status; // mate 모집(구인)중, 모집완료, 삭제됨 등 
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;


  @Data
  public static class PostData {
    private Map<String, Day> days;  // day1, day2, ...
  }

  @Data
  public static class Day {
    private Map<String, Location> locations;  // location1, location2, ...
    private String dayMemo;  // "day memo"
  }

  @Data
  public static class Location {
    private String name;
    private String categoryCode;
    private double latitude;
    private double longitude;
    private String memo;
    private String image;
  }
}