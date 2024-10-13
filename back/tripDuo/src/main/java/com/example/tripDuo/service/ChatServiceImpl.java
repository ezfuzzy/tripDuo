package com.example.tripDuo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import com.example.tripDuo.dto.ChatMessageDto;
import com.example.tripDuo.dto.ChatRoomDto;
import com.example.tripDuo.dto.UserProfileInfoDto;
import com.example.tripDuo.entity.ChatMessage;
import com.example.tripDuo.entity.ChatParticipant;
import com.example.tripDuo.entity.ChatRoom;
import com.example.tripDuo.entity.UserFollow;
import com.example.tripDuo.entity.UserProfileInfo;
import com.example.tripDuo.enums.FollowType;
import com.example.tripDuo.repository.ChatMessageRepository;
import com.example.tripDuo.repository.ChatParticipantRepository;
import com.example.tripDuo.repository.ChatRoomRepository;
import com.example.tripDuo.repository.UserFollowRepository;
import com.example.tripDuo.repository.UserProfileInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ChatServiceImpl implements ChatService {
	
	@Value("${cloud.aws.cloudfront.profile_picture_url}")
	private String PROFILE_PICTURE_CLOUDFRONT_URL;
	
	private ChatRoomRepository chatRoomRepo;
	private ChatMessageRepository chatMessageRepo;
	private ChatParticipantRepository chatParticipantsRepo;
	private UserProfileInfoRepository userProfileInfoRepo;
	private UserFollowRepository userFollowRepo;
    private RedisTemplate<String, ChatMessageDto> redisTemplate;

	public ChatServiceImpl(ChatRoomRepository chatRoomRepo, ChatMessageRepository chatMessageRepo,
			ChatParticipantRepository chatParticipantsRepo, UserProfileInfoRepository userProfileInfoRepo,
			UserFollowRepository userFollowRepo, RedisTemplate redisTemplate) {
		this.chatRoomRepo = chatRoomRepo;
		this.chatMessageRepo = chatMessageRepo;
		this.chatParticipantsRepo = chatParticipantsRepo;
		this.userProfileInfoRepo = userProfileInfoRepo;
		this.userFollowRepo = userFollowRepo;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public List<UserFollow> getSelectUser(Long userId) {
		List<UserFollow> followees = userFollowRepo.findByFolloweeUserProfileInfo_User_IdAndFollowType(userId,
				FollowType.FOLLOW);
		List<UserFollow> followers = userFollowRepo.findByFollowerUserProfileInfo_User_IdAndFollowType(userId,
				FollowType.FOLLOW);

		List<String> followeeIds = followees.stream().map(follow -> follow.getFolloweeUserProfileInfo().getNickname())
				.collect(Collectors.toList());

		List<String> followerIds = followers.stream().map(follow -> follow.getFollowerUserProfileInfo().getNickname())
				.collect(Collectors.toList());

		List<UserFollow> userFollowDtos = new ArrayList<>();

		for (UserFollow follow : followees) {
//			Long followeeId = follow.getFolloweeUserProfileInfo().getUser().getId();
			userFollowDtos.add(follow);
		}
		return userFollowDtos;
	}
	
	@Override
	public List<ChatRoomDto> getSelectAllChatRooms(Long userId) {
		// 사용자가 속한 채팅방 참여 정보를 모두 가져옴
		List<ChatParticipant> chatParticipantsList = chatParticipantsRepo.findByUserProfileInfoUserId(userId);
		// 참가자가 속한 채팅방 ID 목록을 추출
		List<Long> chatRoomIds = chatParticipantsList.stream().map(ChatParticipant::getChatRoomId).collect(Collectors.toList());
		// 한 번에 모든 채팅방 정보를 가져옴
		List<ChatRoom> chatRooms = chatRoomRepo.findByIdInOrderByLastmessagetimeDesc(chatRoomIds);
		// ChatRoom을 ChatRoomDto로 변환
		List<ChatRoomDto> chatRoomDtos = chatRooms.stream().map(ChatRoomDto::toDto).collect(Collectors.toList());
		System.out.println("사용자가 속한 채팅방 목록: " + chatRoomDtos);
		return chatRoomDtos;
	}

	@Override
	public ChatRoomDto getSelectUserChatRoom(Long roomId) {
		// roomId로 채팅방 정보를 조회
		ChatRoom getUserChatroom = chatRoomRepo.findById(roomId)
				.orElseThrow(() -> new RuntimeException("ChatRoom not found"));
		
		// ChatRoomDto로 변환
		ChatRoomDto getUserChatroomDto = ChatRoomDto.toDto(getUserChatroom);

		// 참여자 정보 조회
		List<ChatParticipant> participants = chatParticipantsRepo.findByChatRoomId(roomId);
	    
		return getUserChatroomDto;
	}
	
	// 특정 채팅방의 모든 메시지를 반환
	@Override
	public Map<String, Object> getChatMessages(Long roomId) {
		List<ChatMessage> messageList = chatMessageRepo.findByChatRoomId(roomId);
		List<ChatMessageDto> messageDtoList = new ArrayList<>();
		
		for (ChatMessage message : messageList) {			
			messageDtoList.add(ChatMessageDto.toDto(message));
		}
		System.out.println("메세지 확인"+messageDtoList);
		
		
		return Map.of(
				"list", messageDtoList,
				"PROFILE_PICTURE_CLOUDFRONT_URL", PROFILE_PICTURE_CLOUDFRONT_URL
				);
	}

	// 채팅방 생성
	@Override
	public ChatRoom createChatRoom(ChatRoomDto chatRoomDto) {
		LocalDateTime localTime = LocalDateTime.now();
		chatRoomDto.setLastmessagetime(localTime);
		ChatRoom chatRoom = chatRoomRepo.save(ChatRoom.toEntity(chatRoomDto));
		System.out.println("채팅방 생성됨: " + chatRoom.getId());

		for (Long curUserId : chatRoomDto.getParticipantsList()) {
			UserProfileInfo userProfileInfo = userProfileInfoRepo.findById(curUserId)
					.orElseThrow(() -> new EntityNotFoundException("UserProfileInfo  not Found"));

			boolean isOwner = curUserId == chatRoomDto.getOwnerId();

			ChatParticipant chatParticipant = ChatParticipant.builder().chatRoomId(chatRoom.getId())
					.userProfileInfo(userProfileInfo).isOwner(isOwner).build();
			chatParticipantsRepo.save(chatParticipant);
		}
		return chatRoom;
	}

	@Override
	public ChatMessageDto saveMessageToRedis(TextMessage message) throws JsonProcessingException {
		// 1. 클라이언트로부터 들어온 메시지 수신
		String payload = message.getPayload();
		// 2. 메시지를 ChatMessage 객체로 변환
		ChatMessage chatMessage = new ObjectMapper().readValue(payload, ChatMessage.class);
		// 3. Dto 변환
		ChatMessageDto chatMessageDto = ChatMessageDto.toDto(chatMessage);
		// 4. Redis에 메시지 저장 (캐싱)
		String redisKey = "chatRoomId:" + chatMessageDto.getChatRoomId();
		redisTemplate.opsForList().rightPush(redisKey, chatMessageDto);
		System.out.println("Message saved to Redis with key " + redisKey + ": " + chatMessageDto);

		return chatMessageDto;
	}

	@Override
	@Scheduled(fixedRate = 6000000) // 100분마다 실행
	public void saveMessagesToDatabase() {
		// 모든 채팅방 키 가져오기
		Set<String> chatRooms = redisTemplate.keys("chatRoomId:*");
		System.out.println("Scheduled task running. Found chat rooms: " + chatRooms);

		for (String redisKey : chatRooms) {
			// 메시지 가져오기
			List<ChatMessageDto> messages = redisTemplate.opsForList().range(redisKey, 0, -1);
			if (messages != null && !messages.isEmpty()) {
				for (ChatMessageDto message : messages) {

					// 메시지에서 userId를 가져옴
					Long userId = message.getUserProfileInfo().getId();// message 객체에 userId가 포함되어 있다고 가정

					// userId를 기반으로 UserProfileInfo 조회
					UserProfileInfo userProfileInfo = userProfileInfoRepo.findById(userId)
							.orElseThrow(() -> new RuntimeException("User not found"));

					UserProfileInfoDto dto = UserProfileInfoDto.toDto(userProfileInfo, redisKey);

					ChatMessage toEntityChatMessage = ChatMessage.toEntity(message, userProfileInfo);

					chatMessageRepo.save(toEntityChatMessage); // DB에 메시지 저장
				}
				// 메시지를 DB에 저장한 후 Redis에서 삭제할 경우
				redisTemplate.opsForList().trim(redisKey, messages.size(), -1); // 저장한 메시지 제거
			}
		}
	}

    @PreDestroy
	@Override
	public void clearRedisCacheOnShutdown() {
		// redis에 저장된 모든 채팅방 키 가져오기
		Set<String> chatRooms = redisTemplate.keys("chatRoomId:*");

		if (chatRooms != null && !chatRooms.isEmpty()) {
			// 각 채팅방 키에 해당하는 데이터를 삭제
			for (String redisKey : chatRooms) {
				redisTemplate.delete(redisKey);
				System.out.println("Deleted Redis cache for: " + redisKey);
			}
		}
	}

	@Override
	public ChatMessageDto saveMessageToDatabsePassive(ChatMessageDto chatMessageDto) {
		// 모든 채팅방 키 가져오기
		Set<String> chatRooms = redisTemplate.keys("chatRoomId:*");
		System.out.println("Scheduled task running. Found chat rooms: " + chatRooms);

		for (String redisKey : chatRooms) {
			// 메시지 가져오기
			List<ChatMessageDto> messages = redisTemplate.opsForList().range(redisKey, 0, -1);
			if (messages != null && !messages.isEmpty()) {
				for (ChatMessageDto message : messages) {
					// 메시지에서 userId를 가져옴
					Long userId = message.getUserProfileInfo().getId();// message 객체에 userId가 포함되어 있다고 가정

					// userId를 기반으로 UserProfileInfo 조회
					UserProfileInfo userProfileInfo = userProfileInfoRepo.findById(userId)
							.orElseThrow(() -> new RuntimeException("User not found"));

					UserProfileInfoDto dto = UserProfileInfoDto.toDto(userProfileInfo, redisKey);
					ChatMessage toEntityChatMessage = ChatMessage.toEntity(message, userProfileInfo);							

					ChatMessage saveMessage = chatMessageRepo.save(toEntityChatMessage); // DB에 메시지 저장
					chatMessageDto = chatMessageDto.toDto(saveMessage);
				}
				// 메시지를 DB에 저장한 후 Redis에서 삭제할 경우
				redisTemplate.opsForList().trim(redisKey, messages.size(), -1); // 저장한 메시지 제거
			}		
		}
		return chatMessageDto;
	}

}
