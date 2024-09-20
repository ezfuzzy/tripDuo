package com.example.tripDuo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.tripDuo.dto.UserProfileInfoDto;
import com.example.tripDuo.entity.UserProfileInfo;

public interface UserProfileInfoRepository extends JpaRepository<UserProfileInfo, Long>{
	UserProfileInfo findByUserId(Long userId);
	
	boolean existsByNickname(String nickname);
	
    @Query("SELECT new com.example.tripDuo.dto.UserProfileInfoDto(u.user.id, u.nickname, u.profilePicture, u.profileMessage) " +
           "FROM UserProfileInfo u " +
           "WHERE u.user.id IN :userIds")
    List<UserProfileInfoDto> findProfilesByUserIds(@Param("userIds") List<Long> userIds);
}