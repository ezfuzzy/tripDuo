package com.example.tripDuo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.tripDuo.entity.UserFollow;
import com.example.tripDuo.enums.FollowType;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
	long countByFollowerUserIdAndFollowType(Long followerUserId, FollowType followType); // profile view page에서 해당 유저가 follow 하는 사람이 몇명인지
	long countByFolloweeUserIdAndFollowType(Long followeeUserId, FollowType followType); // profile view page에서 해당 유저를 follow 하는 사람이 몇명인지
	
	@Query("SELECT uf.followerUserId "
			+ "FROM UserFollow uf "
			+ "WHERE uf.followeeUserId = :followeeUserId AND uf.followType = :followType")
	List<Long> findFollowerUserIdsByFolloweeUserIdAndFollowType(@Param("followeeUserId") Long followeeUserId, @Param("followType") FollowType followType);
    
	@Query("SELECT uf.followeeUserId "
			+ "FROM UserFollow uf "
			+ "WHERE uf.followerUserId = :followerUserId AND uf.followType = :followType")
    List<Long> findFolloweeUserIdsByFollowerUserIdAndFollowType(@Param("followerUserId") Long followerUserId, @Param("followType") FollowType followType);
	
	void deleteByFolloweeUserIdAndFollowTypeAndFollowerUserId(Long followeeUserId, FollowType followType, Long followerUserId);
}