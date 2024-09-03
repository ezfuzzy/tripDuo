package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.dto.PostDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

	private String type;
	private String title;
	private String content;
	
	private String locationCountry;
	private String locationCity;
	
	private String tags;
	private Long views; // 조회수  
	private Long likes; // 좋아요 개수
	
	private Float rating;
	private String status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@Column(name = "data", columnDefinition = "TEXT")
	private String data; // JSON 데이터를 문자열로 저장

	private static final ObjectMapper objectMapper = new ObjectMapper();

	// DTO를 엔티티로 변환
	public static Post toEntity(PostDto dto) {
		String jsonData = convertPostDataToJson(dto.getData());

		return Post.builder()
					.id(dto.getId())
					.userId(dto.getUser_id())
					.type(dto.getType())
					.title(dto.getTitle())
					.locationCountry(dto.getCountry())
					.locationCity(dto.getCity())
					.tags(dto.getTags())
					.views(dto.getViews())
					.likes(dto.getLikes())
					.rating(dto.getRating())
					.status(dto.getStatus())
					.createdAt(dto.getCreatedAt())
					.updatedAt(dto.getUpdatedAt())
					.data(jsonData)
					.build();
	}

	// JSON 문자열을 PostData 객체로 변환
	private PostDto.PostData convertJsonToPostData(String jsonData) {
		try {
			return objectMapper.readValue(jsonData, PostDto.PostData.class);
		} catch (JsonMappingException e) {
			// JSON 매핑 에러 처리
			e.printStackTrace();
			return null;
		} catch (JsonProcessingException e) {
			// JSON 처리 에러 처리
			e.printStackTrace();
			return null;
		}
	}

	// PostData 객체를 JSON 문자열로 변환
	private static String convertPostDataToJson(PostDto.PostData postData) {
		try {
			return objectMapper.writeValueAsString(postData);
		} catch (JsonProcessingException e) {
			// JSON 처리 에러 처리
			e.printStackTrace();
			return null;
		}
	}
}