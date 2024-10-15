package com.example.tripDuo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.tripDuo.entity.Report;
import com.example.tripDuo.entity.ReportToChatMessage;
import com.example.tripDuo.entity.ReportToChatRoom;
import com.example.tripDuo.entity.ReportToPost;
import com.example.tripDuo.entity.ReportToPostComment;
import com.example.tripDuo.entity.ReportToUser;
import com.example.tripDuo.entity.ReportToUserReview;

// 동적 쿼리를 사용하기 위해 JpaSpecificationExecutor 인터페이스를 상속
public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {

    // 각 신고 대상에 대한 최근 신고 정보를 가져오는 쿼리
    @Query("SELECT r FROM ReportToUser r WHERE r.reporterId = :reporterId AND r.reportedUser.id = :reportedId ORDER BY r.createdAt DESC")
    List<ReportToUser> findReportsToUser(@Param("reporterId") Long reporterId, @Param("reportedId") Long reportedId, Pageable pageable);

    @Query("SELECT r FROM ReportToUserReview r WHERE r.reporterId = :reporterId AND r.reportedUserReview.id = :reportedId ORDER BY r.createdAt DESC")
    List<ReportToUserReview> findReportsToUserReview(@Param("reporterId") Long reporterId, @Param("reportedId") Long reportedId, Pageable pageable);

    @Query("SELECT r FROM ReportToPost r WHERE r.reporterId = :reporterId AND r.reportedPost.id = :reportedId ORDER BY r.createdAt DESC")
    List<ReportToPost> findReportsToPost(@Param("reporterId") Long reporterId, @Param("reportedId") Long reportedId, Pageable pageable);

    @Query("SELECT r FROM ReportToPostComment r WHERE r.reporterId = :reporterId AND r.reportedPostComment.id = :reportedId ORDER BY r.createdAt DESC")
    List<ReportToPostComment> findReportsToPostComment(@Param("reporterId") Long reporterId, @Param("reportedId") Long reportedId, Pageable pageable);

    @Query("SELECT r FROM ReportToChatRoom r WHERE r.reporterId = :reporterId AND r.reportedChatRoom.id = :reportedId ORDER BY r.createdAt DESC")
    List<ReportToChatRoom> findReportsToChatRoom(@Param("reporterId") Long reporterId, @Param("reportedId") Long reportedId, Pageable pageable);

    @Query("SELECT r FROM ReportToChatMessage r WHERE r.reporterId = :reporterId AND r.reportedChatMessage.id = :reportedId ORDER BY r.createdAt DESC")
    List<ReportToChatMessage> findReportsToChatMessage(@Param("reporterId") Long reporterId, @Param("reportedId") Long reportedId, Pageable pageable);
}