package com.example.tripDuo.service;

import org.springframework.data.jpa.domain.Specification;

import com.example.tripDuo.dto.PostDto;
import com.example.tripDuo.entity.Post;
import com.example.tripDuo.enums.PostStatus;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PostSpecification {
	
	public static Specification<Post> searchPosts(PostDto postDto) {
		
		return (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();

			// root은 post엔티티를 뜻해서 post.get(필드명) 이라고 보면 됨
			
			// Type 검색 조건 (항상 값이 있음)
			predicate = criteriaBuilder.and(predicate, 
					criteriaBuilder.equal(root.get("type"), postDto.getType()));

			// userId 검색 조건 추가
			if (postDto.getUserId() != null) {
				predicate = criteriaBuilder.and(predicate,
						criteriaBuilder.equal(root.get("userProfileInfo").get("id"), postDto.getUserId()));
				
				// 삭제된 데이터 필터링
				predicate = criteriaBuilder.and(predicate,
	                    criteriaBuilder.notEqual(root.get("status"), PostStatus.DELETED));
			} else {

				// 날짜를 통한 검색 
                if ("includes".equals(postDto.getSearchDateCondition()) 
                		&& postDto.getStartDate() != null && postDto.getEndDate() != null) {
                    // startDate와 endDate를 포함하는 postList
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), postDto.getStartDate()),
                            criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), postDto.getEndDate()));
                } else if (postDto.getStartDate() != null && postDto.getEndDate() != null) {
                	// startDate와 endDate에 포함되는 postList
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), postDto.getStartDate()),
                            criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), postDto.getEndDate()));
                }

                // City 검색 조건
                if (postDto.getCity() != null && !postDto.getCity().isEmpty()) {
                	predicate = criteriaBuilder.and(predicate,
                			criteriaBuilder.equal(root.get("city"), postDto.getCity()));
                }
				
				// Country 검색 조건
				if (postDto.getCountry() != null && !postDto.getCountry().isEmpty()) {
					predicate = criteriaBuilder.and(predicate,
							criteriaBuilder.equal(root.get("country"), postDto.getCountry()));
				}

                
				// Keyword 검색 조건
				if (postDto.getKeyword() != null && !postDto.getKeyword().isEmpty()) {
					String keyword = "%" + postDto.getKeyword() + "%";
					if (postDto.getCondition().equals("title")) {
						predicate = criteriaBuilder.and(predicate, 
								criteriaBuilder.like(root.get("title"), keyword));
					} else if (postDto.getCondition().equals("content")) {
						predicate = criteriaBuilder.and(predicate, 
								criteriaBuilder.like(root.get("content"), keyword));
					} else if (postDto.getCondition().equals("title_content")) {
						Predicate titlePredicate = criteriaBuilder.like(root.get("title"), keyword);
						Predicate contentPredicate = criteriaBuilder.like(root.get("content"), keyword);
						predicate = criteriaBuilder.and(predicate,
								criteriaBuilder.or(titlePredicate, contentPredicate));
					} else if (postDto.getCondition().equals("writer")) {
						predicate = criteriaBuilder.and(predicate,
								criteriaBuilder.like(root.get("writer"), keyword));
					}
				}
				
				// Tags 검색 조건
				if (postDto.getTags() != null && postDto.getTags().length > 0) {
					predicate = criteriaBuilder.and(predicate, 
							root.get("tags").in((Object[]) postDto.getTags()));
				}
				
				// 일반적인 검색의 경우 status값이 PRIVATE, DELETED인 데이터들 필터링  
				predicate = criteriaBuilder.and(predicate,
	                    criteriaBuilder.not(root.get("status").in(PostStatus.PRIVATE, PostStatus.DELETED)));
				
			}
			return predicate;
		};
	}
}
