package com.example.tripDuo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.tripDuo.entity.UserFollow;
import com.example.tripDuo.enums.FollowType;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
	long countByFolloweeUserProfileInfo_User_IdAndFollowType(Long followeeUserId, FollowType followType); // profile view page에서 해당 유저를 follow 하는 사람이 몇명인지
	long countByFollowerUserProfileInfo_User_IdAndFollowType(Long followerUserId, FollowType followType); // profile view page에서 해당 유저가 follow 하는 사람이 몇명인지
	
	@Query("SELECT uf.followerUserProfileInfo.user.id FROM UserFollow uf WHERE uf.followeeUserProfileInfo.user.id = :userId AND uf.followType = :followType")
    List<Long> findFollowerUserIdsByFolloweeUserIdAndFollowType(@Param("userId") Long userId, @Param("followType") FollowType followType);
	
	@Query("SELECT uf.followeeUserProfileInfo.user.id FROM UserFollow uf WHERE uf.followerUserProfileInfo.user.id = :userId AND uf.followType = :followType")
	List<Long> findFolloweeUserIdsByFollowerUserIdAndFollowType(@Param("userId") Long userId, @Param("followType") FollowType followType);
	
	UserFollow findByFolloweeUserProfileInfo_User_IdAndFollowerUserProfileInfo_User_Id(Long followeeUserId, Long followerUserId);
	
	List<UserFollow> findByFolloweeUserProfileInfo_User_IdAndFollowType(Long userId, FollowType followType);
	List<UserFollow> findByFollowerUserProfileInfo_User_IdAndFollowType(Long userId, FollowType followType);
		
	void deleteByFolloweeUserProfileInfo_User_IdAndFollowTypeAndFollowerUserProfileInfo_User_Id(Long followeeUserId, FollowType followType, Long followerUserId);
}