package com.example.tripDuo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.UserFollow;
import com.example.tripDuo.enums.FollowType;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long>{
	long countByFollowerUserId(Long followerUserId); // profile view page에서 해당 유저가 follow 하는 사람이 몇명인지
	long countByFolloweeUserId(Long followeeUserId); // profile view page에서 해당 유저를 follow 하는 사람이 몇명인지
	
	List<Long> findFolloweeUserIdsByFollowerUserIdAndFollowType(Long followerUserId, FollowType followType);
    List<Long> findFollowerUserIdsByFolloweeUserIdAndFollowType(Long followeeUserId, FollowType followType);
    void deleteByFolloweeUserIdAndFollowTypeAndFollowerUserId(Long followeeUserId, FollowType followType, Long followerUserId);
}
