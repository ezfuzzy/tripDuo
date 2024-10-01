package com.example.tripDuo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import com.example.tripDuo.dto.ChatMessageDto;
import com.example.tripDuo.dto.ChatRoomDto;
import com.example.tripDuo.entity.ChatMessage;
import com.example.tripDuo.entity.ChatParticipant;
import com.example.tripDuo.entity.ChatRoom;
import com.example.tripDuo.entity.UserProfileInfo;
import com.example.tripDuo.repository.ChatMessageRepository;
import com.example.tripDuo.repository.ChatParticipantRepository;
import com.example.tripDuo.repository.ChatRoomRepository;
import com.example.tripDuo.repository.UserProfileInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ChatServiceImpl implements ChatService {
	private ChatRoomRepository chatRoomRepo;
	private ChatMessageRepository chatMessageRepo;
	private ChatParticipantRepository chatParticipantsRepo;
	private UserProfileInfoRepository userProfileInfoRepo;
    private RedisTemplate<String, ChatMessageDto> redisTemplate;

	public ChatServiceImpl(ChatRoomRepository chatRoomRepo, ChatMessageRepository chatMessageRepo,
			ChatParticipantRepository chatParticipantsRepo, UserProfileInfoRepository userProfileInfoRepo,
			RedisTemplate redisTemplate) {
		this.chatRoomRepo = chatRoomRepo;
		this.chatMessageRepo = chatMessageRepo;
		this.chatParticipantsRepo = chatParticipantsRepo;
		this.userProfileInfoRepo = userProfileInfoRepo;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public List<ChatRoomDto> getSelectAllChatRooms(Long userId) {
		// 사용자가 속한 채팅방 참여 정보를 모두 가져옴
		List<ChatParticipant> chatParticipantsList = chatParticipantsRepo.findByUserProfileInfoUserId(userId);
		System.out.println(chatParticipantsList.toString());

		// 참가자가 속한 채팅방 ID 목록을 추출
		List<Long> chatRoomIds = chatParticipantsList.stream().map(ChatParticipant::getChatRoomId).collect(Collectors.toList());

		// 한 번에 모든 채팅방 정보를 가져옴
		List<ChatRoom> chatRooms = chatRoomRepo.findByIdIn(chatRoomIds);

		// ChatRoom을 ChatRoomDto로 변환
		List<ChatRoomDto> chatRoomDtos = chatRooms.stream().map(ChatRoomDto::toDto).collect(Collectors.toList());

		System.out.println("사용자가 속한 채팅방 목록: " + chatRoomDtos);
		return chatRoomDtos;
	}

	// 특정 채팅방의 모든 메시지를 반환
	@Override
	public List<ChatMessageDto> getChatMessages(Long roomId) {
		List<ChatMessage> messages = chatMessageRepo.findByChatRoomId(roomId);
		List<ChatMessageDto> messageDtos = new ArrayList<>();

		for (ChatMessage message : messages) {
			messageDtos.add(ChatMessageDto.toDto(message));
		}

		return messageDtos;
	}

	// 채팅방 생성
	@Override
	public ChatRoom createChatRoom(ChatRoomDto chatRoomDto) {

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
	@Scheduled(fixedRate = 6000) // 1분마다 실행
	public void saveMessagesToDatabase() {
		// Redis에서 모든 채팅방의 메시지를 가져와 DB에 저장
		Set<String> chatRooms = redisTemplate.keys("chatRoomId:*"); // 모든 채팅방 키 가져오기
		System.out.println("Scheduled task running. Found chat rooms: " + chatRooms);

		for (String redisKey : chatRooms) {
			List<ChatMessageDto> messages = redisTemplate.opsForList().range(redisKey, 0, -1); // 메시지 가져오기
			if (messages != null && !messages.isEmpty()) {
				for (ChatMessageDto message : messages) {

					UserProfileInfo userProfileInfo = userProfileInfoRepo.findById(message.getChatRoomId())
							.orElseThrow(() -> new RuntimeException("User not found"));
					ChatMessageDto chatMessageDto = new ChatMessageDto();
					chatMessageDto.setUserProfileInfo(userProfileInfo);

					ChatMessage toEntityChatMessage = ChatMessage.toEntity(message, userProfileInfo);
					System.out.println("5555");

					chatMessageRepo.save(toEntityChatMessage); // DB에 메시지 저장
					System.out.println("3333");

				}
				// 메시지를 DB에 저장한 후 Redis에서 삭제할 경우
				redisTemplate.opsForList().trim(redisKey, messages.size(), -1); // 저장한 메시지 제거
			}
		}
	}

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

}
