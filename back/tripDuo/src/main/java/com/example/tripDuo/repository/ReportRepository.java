package com.example.tripDuo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.tripDuo.entity.Report;

// 동적 쿼리를 사용하기 위해 JpaSpecificationExecutor 인터페이스를 상속
public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {

    // 각 신고 대상에 대한 최근 신고 정보를 가져오는 쿼리
    @Query("SELECT r FROM Report r WHERE r.reporterId = :reporterId AND TYPE(r) = :reportType AND r.reportedUser.id = :reportedId ORDER BY r.createdAt DESC")
    List<Report> findReportsToUser(@Param("reporterId") Long reporterId, @Param("reportedId") Long reportedId, Pageable pageable);

    @Query("SELECT r FROM Report r WHERE r.reporterId = :reporterId AND TYPE(r) = :reportType AND r.reportedUserReview.id = :reportedId ORDER BY r.createdAt DESC")
    List<Report> findReportsToUserReview(@Param("reporterId") Long reporterId, @Param("reportedId") Long reportedId, Pageable pageable);

    @Query("SELECT r FROM Report r WHERE r.reporterId = :reporterId AND TYPE(r) = :reportType AND r.reportedPost.id = :reportedId ORDER BY r.createdAt DESC")
    List<Report> findReportsToPost(@Param("reporterId") Long reporterId, @Param("reportedId") Long reportedId, Pageable pageable);

    @Query("SELECT r FROM Report r WHERE r.reporterId = :reporterId AND TYPE(r) = :reportType AND r.reportedPostComment.id = :reportedId ORDER BY r.createdAt DESC")
    List<Report> findReportsToPostComment(@Param("reporterId") Long reporterId, @Param("reportedId") Long reportedId, Pageable pageable);

    @Query("SELECT r FROM Report r WHERE r.reporterId = :reporterId AND TYPE(r) = :reportType AND r.reportedChatRoom.id = :reportedId ORDER BY r.createdAt DESC")
    List<Report> findReportsToChatRoom(@Param("reporterId") Long reporterId, @Param("reportedId") Long reportedId, Pageable pageable);

    @Query("SELECT r FROM Report r WHERE r.reporterId = :reporterId AND TYPE(r) = :reportType AND r.reportedChatMessage.id = :reportedId ORDER BY r.createdAt DESC")
    List<Report> findReportsToChatMessage(@Param("reporterId") Long reporterId, @Param("reportedId") Long reportedId, Pageable pageable);
}