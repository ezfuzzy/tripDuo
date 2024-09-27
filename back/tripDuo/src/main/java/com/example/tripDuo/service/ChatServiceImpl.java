package com.example.tripDuo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.tripDuo.dto.ChatMessageDto;
import com.example.tripDuo.dto.ChatRoomDto;
import com.example.tripDuo.entity.ChatMessage;
import com.example.tripDuo.entity.ChatParticipant;
import com.example.tripDuo.entity.ChatRoom;
import com.example.tripDuo.entity.UserProfileInfo;
import com.example.tripDuo.repository.ChatParticipantRepository;
import com.example.tripDuo.repository.ChatRoomRepository;
import com.example.tripDuo.repository.MessageRepository;
import com.example.tripDuo.repository.UserProfileInfoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ChatServiceImpl implements ChatService {
	private ChatRoomRepository chatRoomRepo;
	private MessageRepository messageRepo;
	private ChatParticipantRepository chatParticipantsRepo;
	private UserProfileInfoRepository userProfileInfoRepo;

	public ChatServiceImpl(ChatRoomRepository chatRoomRepo, MessageRepository messageRepo,
			ChatParticipantRepository chatParticipantsRepo, UserProfileInfoRepository userProfileInfoRepo) {
		this.chatRoomRepo = chatRoomRepo;
		this.messageRepo = messageRepo;
		this.chatParticipantsRepo = chatParticipantsRepo;
		this.userProfileInfoRepo = userProfileInfoRepo;
	}

	@Override
	public List<ChatRoomDto> getAllChatRooms(Long userId) {
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
		List<ChatMessage> messages = messageRepo.findByChatRoomId(roomId);
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

}
