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
    @Query("SELECT r FROM Report r WHERE r.reporterId = :reporterId AND TYPE(r) = :reportType AND r.reportedEntity.id = :reportedId ORDER BY r.createdAt DESC")
    List<Report> findReports(@Param("reporterId") Long reporterId, @Param("reportType") Class<? extends Report> reportType, @Param("reportedId") Long reportedId, Pageable pageable);

    // default : 인터페이스 내에서 구현된 메서드로, 인터페이스를 구현하는 클래스에서 선택적으로 오버라이드할 수 있습니다.
    default List<Report> findReportsToUser(Long reporterId, Long reportedId, Pageable pageable) {
        return findReports(reporterId, ReportToUser.class, reportedId, pageable);
    }

    default List<Report> findReportsToUserReview(Long reporterId, Long reportedId, Pageable pageable) {
        return findReports(reporterId, ReportToUserReview.class, reportedId, pageable);
    }

    default List<Report> findReportsToPost(Long reporterId, Long reportedId, Pageable pageable) {
        return findReports(reporterId, ReportToPost.class, reportedId, pageable);
    }

    default List<Report> findReportsToPostComment(Long reporterId, Long reportedId, Pageable pageable) {
        return findReports(reporterId, ReportToPostComment.class, reportedId, pageable);
    }

    default List<Report> findReportsToChatRoom(Long reporterId, Long reportedId, Pageable pageable) {
        return findReports(reporterId, ReportToChatRoom.class, reportedId, pageable);
    }

    default List<Report> findReportsToChatMessage(Long reporterId, Long reportedId, Pageable pageable) {
        return findReports(reporterId, ReportToChatMessage.class, reportedId, pageable);
    }
}