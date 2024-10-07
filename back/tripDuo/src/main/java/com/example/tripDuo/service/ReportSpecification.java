package com.example.tripDuo.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

import org.springframework.data.jpa.domain.Specification;

import com.example.tripDuo.dto.ReportDto;
import com.example.tripDuo.entity.Report;
import com.example.tripDuo.entity.ReportToChatMessage;
import com.example.tripDuo.entity.ReportToChatRoom;
import com.example.tripDuo.entity.ReportToPost;
import com.example.tripDuo.entity.ReportToPostComment;
import com.example.tripDuo.entity.ReportToUser;
import com.example.tripDuo.entity.ReportToUserReview;
import com.example.tripDuo.enums.ReportTarget;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ReportSpecification {
    // Specification 인터페이스는 동적 쿼리를 생성하기 위해 사용
    public static Specification<Report> searchReports(ReportDto reportDto) {
        // root는 쿼리의 FROM 절에 해당하는 엔티티를 나타내는 객체
        // CriteriaQuery는 SQL 쿼리의 구조를 정의하는 데 사용
        // CriteriaBuilder는 동적 쿼리를 생성하는 데 사용
        return (Root<Report> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Predicate는 쿼리의 WHERE 절에 사용되는 조건을 나타내는 객체
            // criteriaBuilder.conjunction()는 여러 조건을 AND로 결합할 수 있는 빈 조건을 생성
            Predicate predicate = criteriaBuilder.conjunction();

            // root은 report 엔티티를 뜻해서 report.get(필드명) 이라고 보면 됨

            // 검색 범위를 빠르게 좁힐 수 있는 순서대로 검색
            // 1. status
            // 2. targetType
            // 3. targetId
            // 4. createdAtMonth

            // status 검색 조건
            if (reportDto.getReportStatus() != null) {
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("status"), reportDto.getReportStatus()));
            }

            // targetType 검색 조건
            if (reportDto.getTargetType() != null) {
                ReportTarget targetEnum = ReportTarget.fromString(reportDto.getTargetType());
                switch (targetEnum) {
                    case USER:
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.type(), ReportToUser.class));
                        break;
                    case USER_REVIEW:
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.type(), ReportToUserReview.class));
                        break;
                    case POST:
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.type(), ReportToPost.class));
                        break;
                    case POST_COMMENT:
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.type(), ReportToPostComment.class));
                        break;
                    case CHAT_ROOM:
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.type(), ReportToChatRoom.class));
                        break;
                    case CHAT_MESSAGE:
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.type(), ReportToChatMessage.class));
                        break;
                }
            }

            // targetId 검색 조건
            if (reportDto.getTargetId() != null) {
                // criteriaBuilder.disjunction()는 여러 조건을 OR로 결합할 수 있는 빈 조건을 생성
                Predicate targetIdPredicate = criteriaBuilder.disjunction();
                targetIdPredicate = criteriaBuilder.or(targetIdPredicate,
                    criteriaBuilder.equal(root.get("reportedUser").get("id"), reportDto.getTargetId()));
                targetIdPredicate = criteriaBuilder.or(targetIdPredicate,
                    criteriaBuilder.equal(root.get("reportedUserReview").get("id"), reportDto.getTargetId()));
                targetIdPredicate = criteriaBuilder.or(targetIdPredicate,
                    criteriaBuilder.equal(root.get("reportedPost").get("id"), reportDto.getTargetId()));
                targetIdPredicate = criteriaBuilder.or(targetIdPredicate,
                    criteriaBuilder.equal(root.get("reportedPostComment").get("id"), reportDto.getTargetId()));
                targetIdPredicate = criteriaBuilder.or(targetIdPredicate,
                    criteriaBuilder.equal(root.get("reportedChatRoom").get("id"), reportDto.getTargetId()));
                targetIdPredicate = criteriaBuilder.or(targetIdPredicate,
                    criteriaBuilder.equal(root.get("reportedChatMessage").get("id"), reportDto.getTargetId()));
                
                predicate = criteriaBuilder.and(predicate, targetIdPredicate);
            }

            // createdAt 검색 조건
            // 년-월 기준으로 검색
            // ex) 2024-10
            if (reportDto.getCreatedAtMonth() != null) {
                YearMonth yearMonth = reportDto.getCreatedAtMonth();
                
                // 해당 월의 첫날과 마지막 날 계산
                LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
                LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
                
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.between(root.get("createdAt"), startOfMonth, endOfMonth));
            }

            return predicate;
        };
    }
}