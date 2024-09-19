package com.example.tripDuo.service;

import org.springframework.data.jpa.domain.Specification;

import com.example.tripDuo.dto.PostDto;
import com.example.tripDuo.entity.Post;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PostSpecification {
	public static Specification<Post> searchPosts(PostDto postDto) {
        return (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            // Type 검색 조건 (항상 값이 있음)
            if (postDto.getType() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("type"), postDto.getType()));
            }
            
            // Tags 검색 조건
            if (postDto.getTags() != null && postDto.getTags().length > 0) {
                predicate = criteriaBuilder.and(predicate,
                        root.get("tags").in((Object[]) postDto.getTags()));
            }

            // Country 검색 조건
            if (postDto.getCountry() != null && !postDto.getCountry().isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("country"), postDto.getCountry()));
            }

            // City 검색 조건
            if (postDto.getCity() != null && !postDto.getCity().isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("city"), postDto.getCity()));
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
                }
            }

            return predicate;
        };
    }
}
