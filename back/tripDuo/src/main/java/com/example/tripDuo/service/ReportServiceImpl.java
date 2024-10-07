package com.example.tripDuo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tripDuo.dto.ChatRoomDto;
import com.example.tripDuo.dto.ReportDto;
import com.example.tripDuo.entity.ChatMessage;
import com.example.tripDuo.entity.ChatRoom;
import com.example.tripDuo.entity.Post;
import com.example.tripDuo.entity.PostComment;
import com.example.tripDuo.entity.Report;
import com.example.tripDuo.entity.ReportToChatMessage;
import com.example.tripDuo.entity.ReportToChatRoom;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.entity.ReportToPost;
import com.example.tripDuo.entity.ReportToPostComment;
import com.example.tripDuo.entity.ReportToUser;
import com.example.tripDuo.entity.ReportToUserReview;
import com.example.tripDuo.entity.UserReview;
import com.example.tripDuo.enums.ReportTarget;
import com.example.tripDuo.repository.ChatMessageRepository;
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
	private final ChatMessageRepository chatMessageRepo;

	final int REPORT_PAGE_SIZE = 10;

	public ReportServiceImpl(ReportRepository reportRepo, UserRepository userRepo,
			UserReviewRepository userReviewRepo, PostRepository postRepo, PostCommentRepository postCommentRepo,
			ChatRoomRepository chatRoomRepo, ChatMessageRepository chatMessageRepo) {

		this.reportRepo = reportRepo;
        this.userRepo = userRepo;
        this.userReviewRepo = userReviewRepo;
        this.postRepo = postRepo;
        this.postCommentRepo = postCommentRepo;
		this.chatRoomRepo = chatRoomRepo;
		this.chatMessageRepo = chatMessageRepo;
    }

	/**
	 * @date : 2024. 10. 07.
	 * @user : 유병한
	 * report: 신고하기
	 * 
	 * @param userReportDto
	 * @param targetEnum
	 * @param targetId
	 * @return Map<String, Object>
	 * 성공 시 : { "isSuccess": true }
	 * 실패 시 : { "isSuccess": false, "message": 실패 내용 }
	 */
	@Transactional
	@Override
	public Map<String, Object> report(ReportDto userReportDto, ReportTarget targetEnum, Long targetId) {
        Report recentReport = null;
		// 첫 번째 페이지의 첫 번째 결과만 가져오도록 제한
        Pageable pageable = PageRequest.of(0, 1);

        switch (targetEnum) {
            case USER:
                recentReport = reportRepo.findReportsToUser(userReportDto.getReporterId(), targetId, pageable)
                                         .stream().findFirst().orElse(null);
                break;
            case USER_REVIEW:
                recentReport = reportRepo.findReportsToUserReview(userReportDto.getReporterId(), targetId, pageable)
                                         .stream().findFirst().orElse(null);
                break;
            case POST:
                recentReport = reportRepo.findReportsToPost(userReportDto.getReporterId(), targetId, pageable)
                                         .stream().findFirst().orElse(null);
                break;
            case POST_COMMENT:
                recentReport = reportRepo.findReportsToPostComment(userReportDto.getReporterId(), targetId, pageable)
                                         .stream().findFirst().orElse(null);
                break;
            case CHAT_ROOM:
                recentReport = reportRepo.findReportsToChatRoom(userReportDto.getReporterId(), targetId, pageable)
                                         .stream().findFirst().orElse(null);
                break;
            case CHAT_MESSAGE:
                recentReport = reportRepo.findReportsToChatMessage(userReportDto.getReporterId(), targetId, pageable)
                                         .stream().findFirst().orElse(null);
                break;
        }

		// 기존 신고가 없거나 가장 최근의 신고일자로부터 24시간이 지난 이후면 insert
		if (recentReport == null || recentReport.getCreatedAt().plusHours(24).isBefore(LocalDateTime.now())) {
			switch (targetEnum) {
				case USER:
					User reportedUser = userRepo.findById(targetId)
						.orElseThrow(() -> new EntityNotFoundException("신고 대상 사용자를 찾을 수 없습니다."));
					ReportToUser userReportToUser = ReportToUser.toEntity(userReportDto, reportedUser);
					reportRepo.save(userReportToUser);
					break;
				case USER_REVIEW:
					UserReview reportedUserReview = userReviewRepo.findById(targetId)
						.orElseThrow(() -> new EntityNotFoundException("신고 대상 리뷰를 찾을 수 없습니다."));
					ReportToUserReview userReportToUserReview = ReportToUserReview.toEntity(userReportDto, reportedUserReview);
					reportRepo.save(userReportToUserReview);
					break;
				case POST:
					Post reportedPost = postRepo.findById(targetId)
						.orElseThrow(() -> new EntityNotFoundException("신고 대상 게시글을 찾을 수 없습니다."));
					ReportToPost userReportToPost = ReportToPost.toEntity(userReportDto, reportedPost);
					reportRepo.save(userReportToPost);
					break;
				case POST_COMMENT:
					PostComment reportedPostComment = postCommentRepo.findById(targetId)
						.orElseThrow(() -> new EntityNotFoundException("신고 대상 댓글을 찾을 수 없습니다."));
					ReportToPostComment userReportToPostComment = ReportToPostComment.toEntity(userReportDto, reportedPostComment);
					reportRepo.save(userReportToPostComment);
					break;
				case CHAT_ROOM:
					ChatRoom reportedChatRoom = chatRoomRepo.findById(targetId)
						.orElseThrow(() -> new EntityNotFoundException("신고 대상 채팅방을 찾을 수 없습니다."));
					ReportToChatRoom userReportToChatRoom = ReportToChatRoom.toEntity(userReportDto, reportedChatRoom);
					reportRepo.save(userReportToChatRoom);
					break;
				case CHAT_MESSAGE:
					ChatMessage reportedChatMessage = chatMessageRepo.findById(targetId)
						.orElseThrow(() -> new EntityNotFoundException("신고 대상 채팅 메시지를 찾을 수 없습니다."));
					ReportToChatMessage userReportToChatMessage = ReportToChatMessage.toEntity(userReportDto, reportedChatMessage);
					reportRepo.save(userReportToChatMessage);
					break;
			}
			// 신고 성공
			return Map.of("isSuccess", true);
		} else { // 24시간이 지나지 않았다면 신고 실패
			return Map.of("isSuccess", false, "message", "이미 신고한 게시물은 24시간 후에 다시 신고할 수 있습니다.");
		}
	}

	/**
	 * @date : 2024. 10. 04.
	 * @user : 유병한
	 * getReportList: 신고 정보 가져오기
	 * 검색 가능 : status, createdAtMonth, targetType, targetId, pageNum
	 * 정렬 가능 : sortBy=createdAt_asc, createdAt_desc
	 * 
	 * ex) /api/v1/reports?status=UNPROCESSED&
	 *					   createdAtMonth=2024-10&
	 *					   targetType=USER&
	 *					   targetId=1&
	 *					   pageNum=1& (생략시 1)
	 *					   sortBy=createdAt_asc (생략시 createdAt_desc)
	 * 
	 * @param reportDto
	 * @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> getReportList(ReportDto reportDto) {

		String sortBy = reportDto.getSortBy() != null ? reportDto.getSortBy() : "createdAt_desc";
		
	    Sort sort = switch (sortBy) {
			// 오래된순
	        case "createdAt_asc" -> Sort.by(Sort.Direction.ASC, "createdAt");

			// 최신순
	        case "createdAt_desc" -> Sort.by(Sort.Direction.DESC, "createdAt");
	        default -> Sort.by(Sort.Direction.DESC, "createdAt");
	    };
	    	    
		Pageable pageable = PageRequest.of(reportDto.getPageNum() - 1, REPORT_PAGE_SIZE, sort);
		
		Page<Report> reports = reportRepo.findAll(ReportSpecification.searchReports(reportDto), pageable);
		List<ReportDto> reportList = reports.stream().map(ReportDto::toDto).toList();
		
		// page객체는 페이징처리 전의 전체 데이터 개수를 가지고있음
	    long totalRowCount = reports.getTotalElements();
		int totalReportPages = (int) Math.ceil((double) totalRowCount / REPORT_PAGE_SIZE);
		
		return Map.of(
				"list", reportList, 
				"pageNum", reportDto.getPageNum(),
				"totalRowCount", totalRowCount, // 검색 데이터 개수 
				"totalReportPages", totalReportPages // 총 페이지 수
		);
	}

	/**
	 * @date : 2024. 10. 05.
	 * @user : 유병한
	 * processReport: 신고 처리하기
	 * 
	 * reportStatus: 신고 상태(PROCESSED, UNPROCESSED, PENDING)
	 * accountStatus: 계정 상태(ACTIVE, INACTIVE, WARNED, SUSPENDED)
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

		// reportId로 신고 찾기
		Report existReport = reportRepo.findById(reportDto.getId())
		.orElseThrow(() -> new EntityNotFoundException("신고를 찾을 수 없습니다."));

		if(reportDto.getAccountStatus() != null) {
			// 신고 상태 변경
			existReport.updateStatus(reportDto.getReportStatus());
		}

		if(reportDto.getReportStatus() != null) {
			// 신고 대상 찾기
			ReportDto existReportDto = ReportDto.toDto(existReport);
			User reportedUser = null;
			switch (ReportTarget.fromString(existReportDto.getTargetType())) {
				case USER:
					reportedUser = userRepo.findById(existReportDto.getTargetId())
						.orElseThrow(() -> new EntityNotFoundException("신고 대상 사용자를 찾을 수 없습니다."));
					break;
				case USER_REVIEW:
					UserReview reportedUserReview = userReviewRepo.findById(existReportDto.getTargetId())
						.orElseThrow(() -> new EntityNotFoundException("신고 대상 리뷰를 찾을 수 없습니다."));
					reportedUser = reportedUserReview.getReviewerUserProfileInfo().getUser();
					break;
				case POST:
					Post reportedPost = postRepo.findById(existReportDto.getTargetId())
						.orElseThrow(() -> new EntityNotFoundException("신고 대상 게시글을 찾을 수 없습니다."));
					reportedUser = reportedPost.getUserProfileInfo().getUser();
					break;
				case POST_COMMENT:
					PostComment reportedPostComment = postCommentRepo.findById(existReportDto.getTargetId())
						.orElseThrow(() -> new EntityNotFoundException("신고 대상 댓글을 찾을 수 없습니다."));
					reportedUser = reportedPostComment.getUserProfileInfo().getUser();
					break;
				case CHAT_ROOM:
					ChatRoom reportedChatRoom = chatRoomRepo.findById(existReportDto.getTargetId())
						.orElseThrow(() -> new EntityNotFoundException("신고 대상 채팅방을 찾을 수 없습니다."));
					Long chatRoomOwnerId = ChatRoomDto.toDto(reportedChatRoom).getOwnerId();
					reportedUser = userRepo.findById(chatRoomOwnerId)
						.orElseThrow(() -> new EntityNotFoundException("신고 대상 사용자를 찾을 수 없습니다."));
					break;
				case CHAT_MESSAGE:
					ChatMessage reportedChatMessage = chatMessageRepo.findById(existReportDto.getTargetId())
						.orElseThrow(() -> new EntityNotFoundException("신고 대상 채팅 메시지를 찾을 수 없습니다."));
					reportedUser = reportedChatMessage.getUserProfileInfo().getUser();
					break;
			}
			
			// 신고당한 유저 계정 상태 변경
			reportedUser.updateAccountStatus(reportDto.getAccountStatus());
		}
	}
}