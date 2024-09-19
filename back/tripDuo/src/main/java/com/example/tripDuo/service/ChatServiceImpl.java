package com.example.tripDuo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tripDuo.dto.ChatMessageDto;
import com.example.tripDuo.dto.ChatRoomDto;
import com.example.tripDuo.entity.ChatMessage;
import com.example.tripDuo.entity.ChatRoom;
import com.example.tripDuo.enums.ChatType;
import com.example.tripDuo.repository.ChatRoomRepository;
import com.example.tripDuo.repository.MessageRepository;

@Service
public class ChatServiceImpl implements ChatService {
	private ChatRoomRepository chatRoomRepository;
	private MessageRepository messageRepository;

	
	public ChatServiceImpl(ChatRoomRepository chatRoomRepository, MessageRepository messageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
    }
	
	@Override
	public List<ChatRoomDto> getAllChatRooms() {
		List<ChatRoom> chatRooms = chatRoomRepository.findAll();
	    List<ChatRoomDto> chatRoomDtos = new ArrayList<>();
	    
	    for (ChatRoom room : chatRooms) {
	        ChatMessage lastMessage = messageRepository.findTopByChatRoomOrderByTimestampDesc(room);
	        ChatRoomDto chatRoomDto = ChatRoomDto.toDto(room, lastMessage);
	        chatRoomDtos.add(chatRoomDto);
	    }
	    
	    System.out.println("채팅방 목록 : "+chatRoomDtos);
	    return chatRoomDtos;
	}
	  
	@Override
	public ChatRoomDto getChatRoom(Long id, ChatMessage lastMessage) {
		ChatRoom chatRoom = chatRoomRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("ChatRoom not found"));
		return ChatRoomDto.toDto(chatRoom, lastMessage);
	}

	@Override
	public ChatMessageDto sendMessage(ChatMessageDto chatMessageDto) {
		 ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomId())
		            .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
		 System.out.println("11");
		    ChatMessage message = ChatMessage.toEntity(chatMessageDto, chatRoom);
		    messageRepository.save(message);

		    return chatMessageDto;
		}

    // 추가된 부분: 특정 채팅방의 모든 메시지를 반환
	@Override
	public List<ChatMessageDto> getChatMessages(Long roomId) {
//		 ChatRoom chatRoom = chatRoomRepository.findById(roomId)
//	                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
//		 System.out.println("채팅 메세지 : "+chatRoom.getMessages().stream().map(ChatMessageDto::toDto).toList());
//	        return chatRoom.getMessages().stream().map(ChatMessageDto::toDto).toList(); 
		 ChatRoom chatRoom = chatRoomRepository.findById(roomId)
	                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
	        List<ChatMessageDto> messageDtos = chatRoom.getMessages().stream()
	                .map(ChatMessageDto::toDto)
	                .toList();
	        System.out.println("채팅 메세지 : " + messageDtos);
	        return messageDtos;
	
	}

	@Override
	public ChatRoomDto createChatRoom(String name, ChatType type, ChatMessage lastMessage) {
		  ChatRoom chatRoom = ChatRoom.builder()
		            .name(name)
		            .type(type)
		            .build();
		    chatRoomRepository.save(chatRoom);
		    return ChatRoomDto.toDto(chatRoom, lastMessage);
	}

}
