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
  private PostData data;  // JSON 데이터를 객체로 처리

  private Long user_id;
  private String type;
  private String locationCountry;
  private String locationCity;
  private String tags;
  private Long views;
  private Long likes;
  private Float rating;
  private String status;
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