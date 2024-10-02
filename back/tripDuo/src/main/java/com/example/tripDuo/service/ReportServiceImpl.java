package com.example.tripDuo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tripDuo.dto.ReportDto;
import com.example.tripDuo.dto.ReportToChatMessageDto;
import com.example.tripDuo.dto.ReportToChatRoomDto;
import com.example.tripDuo.dto.ReportToPostCommentDto;
import com.example.tripDuo.dto.ReportToPostDto;
import com.example.tripDuo.dto.ReportToUserDto;
import com.example.tripDuo.dto.ReportToUserReviewDto;
import com.example.tripDuo.entity.ChatMessage;
import com.example.tripDuo.entity.ChatRoom;
import com.example.tripDuo.entity.Post;
import com.example.tripDuo.entity.PostComment;
import com.example.tripDuo.entity.ReportToChatMessage;
import com.example.tripDuo.entity.ReportToChatRoom;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.entity.ReportToPost;
import com.example.tripDuo.entity.ReportToPostComment;
import com.example.tripDuo.entity.ReportToUser;
import com.example.tripDuo.entity.ReportToUserReview;
import com.example.tripDuo.entity.UserReview;
import com.example.tripDuo.enums.ReportStatus;
import com.example.tripDuo.enums.ReportTarget;
import com.example.tripDuo.repository.ChatMessageRepository;
import com.example.tripDuo.repository.ChatRoomRepository;
import com.example.tripDuo.repository.PostCommentRepository;
import com.example.tripDuo.repository.PostRepository;
import com.example.tripDuo.repository.UserReportRepository;
import com.example.tripDuo.repository.UserRepository;
import com.example.tripDuo.repository.UserReviewRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ReportServiceImpl implements ReportService {
	private final UserReportRepository userReportRepo;
	private final UserRepository userRepo;
	private final UserReviewRepository userReviewRepo;
	private final PostRepository postRepo;
	private final PostCommentRepository postCommentRepo;
	private final ChatRoomRepository chatRoomRepo;
	private final ChatMessageRepository chatMessageRepo;

    public ReportServiceImpl(UserReportRepository userReportRepo, UserRepository userRepo,
            UserReviewRepository userReviewRepo, PostRepository postRepo,
            PostCommentRepository postCommentRepo, ChatRoomRepository chatRoomRepo,
			ChatMessageRepository chatMessageRepo) {
                
        this.userReportRepo = userReportRepo;
        this.userRepo = userRepo;
        this.userReviewRepo = userReviewRepo;
        this.postRepo = postRepo;
        this.postCommentRepo = postCommentRepo;
		this.chatRoomRepo = chatRoomRepo;
		this.chatMessageRepo = chatMessageRepo;
    }

	/**
	 * @date : 2024. 10. 01.
	 * @user : 유병한
	 * report: 신고하기
	 * 
	 * @param userReportDto
	 * @param targetEnum
	 * @param targetId
	 * 
	 * @return UserReportDto
	 */
	@Transactional
	@Override
	public ReportDto report(ReportDto userReportDto, ReportTarget targetEnum, Long targetId) {
		userReportDto.setStatus(ReportStatus.UNPROCESSED);

		ReportDto insertedUserReportDto = null;
		switch (targetEnum) {
			case USER:
				User reportedUser = userRepo.findById(targetId)
					.orElseThrow(() -> new EntityNotFoundException("신고 대상 사용자를 찾을 수 없습니다."));
				ReportToUser userReportToUser = ReportToUser.toEntity(userReportDto, reportedUser);
				userReportRepo.save(userReportToUser);
				insertedUserReportDto = ReportToUserDto.toDto(userReportToUser);
				break;
			case USER_REVIEW:
				UserReview reportedUserReview = userReviewRepo.findById(targetId)
					.orElseThrow(() -> new EntityNotFoundException("신고 대상 리뷰를 찾을 수 없습니다."));
				ReportToUserReview userReportToUserReview = ReportToUserReview.toEntity(userReportDto, reportedUserReview);
				userReportRepo.save(userReportToUserReview);
				insertedUserReportDto = ReportToUserReviewDto.toDto(userReportToUserReview);
				break;
			case POST:
				Post reportedPost = postRepo.findById(targetId)
					.orElseThrow(() -> new EntityNotFoundException("신고 대상 게시글을 찾을 수 없습니다."));
				ReportToPost userReportToPost = ReportToPost.toEntity(userReportDto, reportedPost);
				userReportRepo.save(userReportToPost);
				insertedUserReportDto = ReportToPostDto.toDto(userReportToPost);
				break;
			case POST_COMMENT:
				PostComment reportedPostComment = postCommentRepo.findById(targetId)
					.orElseThrow(() -> new EntityNotFoundException("신고 대상 댓글을 찾을 수 없습니다."));
				ReportToPostComment userReportToPostComment = ReportToPostComment.toEntity(userReportDto, reportedPostComment);
				userReportRepo.save(userReportToPostComment);
				insertedUserReportDto = ReportToPostCommentDto.toDto(userReportToPostComment);
				break;
			case CHAT_ROOM:
				ChatRoom reportedChatRoom = chatRoomRepo.findById(targetId)
					.orElseThrow(() -> new EntityNotFoundException("신고 대상 채팅방을 찾을 수 없습니다."));
				ReportToChatRoom userReportToChatRoom = ReportToChatRoom.toEntity(userReportDto, reportedChatRoom);
				userReportRepo.save(userReportToChatRoom);
				insertedUserReportDto = ReportToChatRoomDto.toDto(userReportToChatRoom);
				break;
			case CHAT_MESSAGE:
				ChatMessage reportedChatMessage = chatMessageRepo.findById(targetId)
					.orElseThrow(() -> new EntityNotFoundException("신고 대상 채팅 메시지를 찾을 수 없습니다."));
				ReportToChatMessage userReportToChatMessage = ReportToChatMessage.toEntity(userReportDto, reportedChatMessage);
				userReportRepo.save(userReportToChatMessage);
				insertedUserReportDto = ReportToChatMessageDto.toDto(userReportToChatMessage);
				break;
		}
		return insertedUserReportDto;
	}
}