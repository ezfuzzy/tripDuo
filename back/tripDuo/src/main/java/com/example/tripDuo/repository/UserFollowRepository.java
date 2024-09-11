package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.UserFollow;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long>{
	long countByFollowerUserId(Long followerUserId); // profile view page에서 해당 유저가 follow 하는 사람이 몇명인지
	long countByFolloweeUserId(Long followeeUserId); // profile view page에서 해당 유저를 follow 하는 사람이 몇명인지
}
