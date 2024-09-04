package com.example.tripDuo.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name = "POSTS")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String type; // mate / course / 여행기 / 커뮤니티

    private String title;
    private String content; // 메이트, 커뮤니티 게시글
    private String country;
    private String city;

    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private List<String> tags;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private List<Day> days;

    private Long viewCount;
    private Long likeCount;
    private Float rating;

    private String status; // mate 모집(구인)중, 모집완료, 삭제됨 등

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Entity(name = "DAYS")
    @Data
    public static class Day {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "day_id")
        private List<Place> places;

        private String dayMemo;
    }

    @Entity(name = "PLACES")
    @Data
    public static class Place {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String addressName;
        private String categoryGroupCode;
        private String categoryGroupName;
        private String categoryName;
        private String placeId; // 'id'를 'placeId'로 변경하여 충돌 방지
        private String phone;
        private String placeName;
        private String placeUrl;
        private String roadAddressName;

        @Embedded
        private Position position;

        private int dayIndex;
        private int placeIndex;
        private String placeMemo;
    }

    @Embeddable
    @Data
    public static class Position {
        private double latitude;
        private double longitude;
    }
}
