package com.example.tripDuo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tripDuo.dto.ReportDto;
import com.example.tripDuo.entity.ChatMessage;
import com.example.tripDuo.entity.ChatParticipant;
import com.example.tripDuo.entity.ChatRoom;
import com.example.tripDuo.entity.Post;
import com.example.tripDuo.entity.PostComment;
import com.example.tripDuo.entity.Report;
import com.example.tripDuo.entity.ReportToChatMessage;
import com.example.tripDuo.entity.ReportToChatRoom;
import com.example.tripDuo.entity.ReportToPost;
import com.example.tripDuo.entity.ReportToPostComment;
import com.example.tripDuo.entity.ReportToUser;
import com.example.tripDuo.entity.ReportToUserReview;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.entity.UserReview;
import com.example.tripDuo.enums.AccountStatus;
import com.example.tripDuo.enums.ReportTarget;
import com.example.tripDuo.repository.ChatMessageRepository;
import com.example.tripDuo.repository.ChatParticipantRepository;
import com.example.tripDuo.repository.ChatRoomRepository;
import com.example.tripDuo.repository.PostCommentRepository;
import com.example.tripDuo.repository.PostRepository;
import com.example.tripDuo.repository.ReportRepository;
import com.example.tripDuo.repository.UserRepository;
import com.example.tripDuo.repository.UserReviewRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepo;
    private final UserRepository userRepo;
    private final UserReviewRepository userReviewRepo;
    private final PostRepository postRepo;
    private final PostCommentRepository postCommentRepo;
    private final ChatRoomRepository chatRoomRepo;
    private final ChatParticipantRepository chatParticipantRepo;
    private final ChatMessageRepository chatMessageRepo;

    final int REPORT_PAGE_SIZE = 10;

    public ReportServiceImpl(ReportRepository reportRepo, UserRepository userRepo,
                             UserReviewRepository userReviewRepo, PostRepository postRepo,
                             PostCommentRepository postCommentRepo, ChatRoomRepository chatRoomRepo,
                             ChatParticipantRepository chatParticipantRepo, ChatMessageRepository chatMessageRepo) {

        this.reportRepo = reportRepo;
        this.userRepo = userRepo;
        this.userReviewRepo = userReviewRepo;
        this.postRepo = postRepo;
        this.postCommentRepo = postCommentRepo;
        this.chatRoomRepo = chatRoomRepo;
        this.chatParticipantRepo = chatParticipantRepo;
        this.chatMessageRepo = chatMessageRepo;
    }

    /**
     * report: 신고하기
     * 
     * @param userReportDto
     * @param targetEnum
     * @param targetId
     * @return Map<String, Object>
     * 성공 시 : { "isSuccess": true }
     * 실패 시 : { "isSuccess": false, "message": 실패 내용 }
     */
	// @Transactional : 메서드가 호출될 때 트랜잭션이 시작되고, 메서드가 정상적으로 완료되면 트랜잭션이 커밋됩니다.
	// 					예외가 발생하면 트랜잭션이 롤백됩니다.
    @Transactional
    @Override
    public Map<String, Object> report(ReportDto userReportDto, ReportTarget targetEnum, Long targetId) {
		// 최근 신고 정보 가져오기
        Report recentReport = findRecentReport(userReportDto.getReporterId(), targetEnum, targetId);

		// 최근 신고 정보가 없거나, 최근 신고로부터 24시간이 지났다면 신고 정보 저장
        if (recentReport == null || recentReport.getCreatedAt().plusHours(24).isBefore(LocalDateTime.now())) {
            saveReport(userReportDto, targetEnum, targetId);
            return Map.of("isSuccess", true);
        } else {
            return Map.of("isSuccess", false, "message", "이미 신고한 게시물은 24시간 후에 다시 신고할 수 있습니다.");
        }
    }

    private Report findRecentReport(Long reporterId, ReportTarget targetEnum, Long targetId) {
		// 첫번째 페이지의 첫번째 결과만 가져오기
        Pageable pageable = PageRequest.of(0, 1);

        // 신고 대상 별로 최근 신고 정보 가져오기
        return switch (targetEnum) {
            case USER -> reportRepo.findReportsToUser(reporterId, targetId, pageable).stream().findFirst().orElse(null);
            case USER_REVIEW -> reportRepo.findReportsToUserReview(reporterId, targetId, pageable).stream().findFirst().orElse(null);
            case POST -> reportRepo.findReportsToPost(reporterId, targetId, pageable).stream().findFirst().orElse(null);
            case POST_COMMENT -> reportRepo.findReportsToPostComment(reporterId, targetId, pageable).stream().findFirst().orElse(null);
            case CHAT_ROOM -> reportRepo.findReportsToChatRoom(reporterId, targetId, pageable).stream().findFirst().orElse(null);
            case CHAT_MESSAGE -> reportRepo.findReportsToChatMessage(reporterId, targetId, pageable).stream().findFirst().orElse(null);
            default -> throw new IllegalArgumentException("Invalid report target");
        };
    }

    private void saveReport(ReportDto userReportDto, ReportTarget targetEnum, Long targetId) {
		// 신고 대상 별로 신고 정보 저장
        switch (targetEnum) {
            case USER -> {
                User reportedUser = userRepo.findById(targetId)
                        // orElseThrow : Optional 객체가 값을 가지고 있으면 그 값을 반환,
                        // 값이 없으면 예외 발생
                        .orElseThrow(() -> new EntityNotFoundException("신고 대상 사용자를 찾을 수 없습니다."));
                reportRepo.save(ReportToUser.toEntity(userReportDto, reportedUser));
            }
            case USER_REVIEW -> {
                UserReview reportedUserReview = userReviewRepo.findById(targetId)
                        .orElseThrow(() -> new EntityNotFoundException("신고 대상 리뷰를 찾을 수 없습니다."));
                reportRepo.save(ReportToUserReview.toEntity(userReportDto, reportedUserReview));
            }
            case POST -> {
                Post reportedPost = postRepo.findById(targetId)
                        .orElseThrow(() -> new EntityNotFoundException("신고 대상 게시글을 찾을 수 없습니다."));
                reportRepo.save(ReportToPost.toEntity(userReportDto, reportedPost));
            }
            case POST_COMMENT -> {
                PostComment reportedPostComment = postCommentRepo.findById(targetId)
                        .orElseThrow(() -> new EntityNotFoundException("신고 대상 댓글을 찾을 수 없습니다."));
                reportRepo.save(ReportToPostComment.toEntity(userReportDto, reportedPostComment));
            }
            case CHAT_ROOM -> {
                ChatRoom reportedChatRoom = chatRoomRepo.findById(targetId)
                        .orElseThrow(() -> new EntityNotFoundException("신고 대상 채팅방을 찾을 수 없습니다."));
                reportRepo.save(ReportToChatRoom.toEntity(userReportDto, reportedChatRoom));
            }
            case CHAT_MESSAGE -> {
                ChatMessage reportedChatMessage = chatMessageRepo.findById(targetId)
                        .orElseThrow(() -> new EntityNotFoundException("신고 대상 채팅 메시지를 찾을 수 없습니다."));
                reportRepo.save(ReportToChatMessage.toEntity(userReportDto, reportedChatMessage));
            }
        }
    }

    /**
     * getReportList: 신고 정보 가져오기
     * 검색 가능 : status, createdAtMonth, targetType, targetId, pageNum
     * 정렬 가능 : sortBy=createdAt_asc, createdAt_desc
     * 
     * ex) /api/v1/reports?status=UNPROCESSED& (생략 가능)
     *					   createdAtMonth=2024-10& (생략 가능)
     *					   targetType=USER& (생략 가능)
     *					   targetId=1& (생략 가능)
     *					   pageNum=1& (생략시 1)
     *					   sortBy=createdAt_asc (생략시 createdAt_desc)
     * 
     * @param reportDto
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> getReportList(ReportDto reportDto) {
		// 정렬 기본값 설정
        String sortBy = reportDto.getSortBy() != null ? reportDto.getSortBy() : "createdAt_desc";

		// 정렬 기준 설정
        Sort sort = Sort.by(sortBy.equals("createdAt_asc") ? Sort.Direction.ASC : Sort.Direction.DESC, "createdAt");

		// 페이지 번호 설정
        Pageable pageable = PageRequest.of(reportDto.getPageNum() - 1, REPORT_PAGE_SIZE, sort);

		// 신고 정보 가져오기
        Page<Report> reports = reportRepo.findAll(ReportSpecification.searchReports(reportDto), pageable);
        List<Report> reportList = reports.getContent();

        
		List<ReportDto> reportDtoList = reportList.stream()
                .map(ReportDto::toDto)
                .collect(Collectors.toList());

        // targetTypeList, targetIdList, targetAccountStatusList 가져오기
        List<String> targetTypeList = reportDtoList.stream()
                .map(ReportDto::getTargetType)
                .collect(Collectors.toList());

        List<Long> targetIdList = reportDtoList.stream()
                .map(ReportDto::getTargetId)
                .collect(Collectors.toList());

        List<AccountStatus> targetAccountStatusList = reportList.stream()
        .map(this::getAccountStatusFromReport)
        .collect(Collectors.toList());

		// 총 행 수, 페이지 수 계산
        long totalRowCount = reports.getTotalElements();
        int totalReportPages = (int) Math.ceil((double) totalRowCount / REPORT_PAGE_SIZE);

        return Map.of(
                "list", reportList,
                "targetTypeList", targetTypeList,
                "targetIdList", targetIdList,
                "targetAccountStatusList", targetAccountStatusList,
                "pageNum", reportDto.getPageNum(),
                "totalRowCount", totalRowCount,
                "totalReportPages", totalReportPages
        );
    }

    private AccountStatus getAccountStatusFromReport(Report report) {
        // 신고 대상 소유자의 계정 상태 가져오기
        if (report instanceof ReportToUser r) {
            return r.getReportedUser().getAccountStatus();
        } else if (report instanceof ReportToUserReview r) {
            return r.getReportedUserReview().getReviewerUserProfileInfo().getUser().getAccountStatus();
        } else if (report instanceof ReportToPost r) {
            return r.getReportedPost().getUserProfileInfo().getUser().getAccountStatus();
        } else if (report instanceof ReportToPostComment r) {
            return r.getReportedPostComment().getUserProfileInfo().getUser().getAccountStatus();
        } else if (report instanceof ReportToChatRoom r) {
            Long chatRoomId = r.getReportedChatRoom().getId();
            ChatParticipant chatParticipant = chatParticipantRepo.findByChatRoomIdAndIsOwner(chatRoomId, true);
            return chatParticipant.getUserProfileInfo().getUser().getAccountStatus();
        } else if (report instanceof ReportToChatMessage r) {
            return r.getReportedChatMessage().getUserProfileInfo().getUser().getAccountStatus();
        } else {
            return null; // 다른 서브클래스의 경우 null 반환
        }
    }

    /**
     * processReport: 신고 처리하기
     * 
     * reportStatus: 신고 상태(PROCESSED-처리됨, UNPROCESSED-처리되지 않음, PENDING-보류 중)
     * accountStatus: 계정 상태(ACTIVE-활성, INACTIVE-비활성, WARNED-경고, SUSPENDED-정지)
     * 
     * ex)
     * axios.put("/api/v1/reports/1", {
     * 		reportStatus: "PROCESSED",
     * 		accountStatus: "WARNED"
     * })
     * 
     * @param reportDto
     */
    @Transactional
    @Override
    public void processReport(ReportDto reportDto) {
		// 신고 정보 가져오기
        Report existReport = reportRepo.findById(reportDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("신고를 찾을 수 없습니다."));

		// 신고 상태 업데이트
        if (reportDto.getAccountStatus() != null) {
            existReport.updateStatus(reportDto.getReportStatus());
        }

		// 신고 대상 계정 상태 업데이트
        if (reportDto.getReportStatus() != null) {
            updateReportedAccountStatus(existReport, reportDto);
        }
    }

    private void updateReportedAccountStatus(Report existReport, ReportDto reportDto) {
        ReportDto existReportDto = ReportDto.toDto(existReport);

		// 신고 대상 별로 신고 대상 계정 가져오기
        User reportedUser = switch (ReportTarget.fromString(existReportDto.getTargetType())) {
            case USER -> userRepo.findById(existReportDto.getTargetId())
                    .orElseThrow(() -> new EntityNotFoundException("신고 대상 사용자를 찾을 수 없습니다."));
            case USER_REVIEW -> {
                UserReview reportedUserReview = userReviewRepo.findById(existReportDto.getTargetId())
                        .orElseThrow(() -> new EntityNotFoundException("신고 대상 리뷰를 찾을 수 없습니다."));
                // yield : switch용 return
				yield reportedUserReview.getReviewerUserProfileInfo().getUser();
            }
            case POST -> {
                Post reportedPost = postRepo.findById(existReportDto.getTargetId())
                        .orElseThrow(() -> new EntityNotFoundException("신고 대상 게시글을 찾을 수 없습니다."));
                yield reportedPost.getUserProfileInfo().getUser();
            }
            case POST_COMMENT -> {
                PostComment reportedPostComment = postCommentRepo.findById(existReportDto.getTargetId())
                        .orElseThrow(() -> new EntityNotFoundException("신고 대상 댓글을 찾을 수 없습니다."));
                yield reportedPostComment.getUserProfileInfo().getUser();
            }
            case CHAT_ROOM -> {
                ChatRoom reportedChatRoom = chatRoomRepo.findById(existReportDto.getTargetId())
                        .orElseThrow(() -> new EntityNotFoundException("신고 대상 채팅방을 찾을 수 없습니다."));
                ChatParticipant chatParticipant = chatParticipantRepo.findByChatRoomIdAndIsOwner(reportedChatRoom.getId(), true);
                yield chatParticipant.getUserProfileInfo().getUser();
            }
            case CHAT_MESSAGE -> {
                ChatMessage reportedChatMessage = chatMessageRepo.findById(existReportDto.getTargetId())
                        .orElseThrow(() -> new EntityNotFoundException("신고 대상 채팅 메시지를 찾을 수 없습니다."));
                yield reportedChatMessage.getUserProfileInfo().getUser();
            }
        };

		// 신고 대상 계정 상태 업데이트
        reportedUser.updateAccountStatus(reportDto.getAccountStatus());
    }
}