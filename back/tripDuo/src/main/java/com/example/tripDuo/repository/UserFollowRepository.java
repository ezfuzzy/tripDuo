package com.example.tripDuo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.UserFollow;
import com.example.tripDuo.enums.FollowType;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
	// profile view page에서 해당 유저를 follow 하는 사람이 몇명인지
	long countByFolloweeUserProfileInfo_User_IdAndFollowType(Long followeeUserId, FollowType followType); 

	// profile view page에서 해당 유저가 follow 하는 사람이 몇명인지
	long countByFollowerUserProfileInfo_User_IdAndFollowType(Long followerUserId, FollowType followType); 
	
	// add 할 때 기존 값이 있는지 찾는 메소드 
	UserFollow findByFolloweeUserProfileInfo_User_IdAndFollowerUserProfileInfo_User_Id(Long followeeUserId, Long followerUserId);
	void deleteByFolloweeUserProfileInfo_User_IdAndFollowerUserProfileInfo_User_IdAndFollowType(Long follweeUserId, Long followerUserId, FollowType followType);
	
	// delete시 팔로우관계 삭제
	void deleteAllByFolloweeUserProfileInfo_User_Id(Long userId);
	void deleteAllByFollowerUserProfileInfo_User_Id(Long userId);
	
	// get 할 때 팔로워/팔로이 정보들을 가져오는 메소드
	List<UserFollow> findByFolloweeUserProfileInfo_User_IdAndFollowType(Long userId, FollowType followType);
	List<UserFollow> findByFollowerUserProfileInfo_User_IdAndFollowType(Long userId, FollowType followType);
		
	// 팔로우 관계를 delete하는 메소드
	void deleteByFolloweeUserProfileInfo_User_IdAndFollowTypeAndFollowerUserProfileInfo_User_Id(Long followeeUserId, FollowType followType, Long followerUserId);
}