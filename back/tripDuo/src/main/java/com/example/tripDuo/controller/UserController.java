package com.example.tripDuo.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.dto.UserFollowDto;
import com.example.tripDuo.dto.UserProfileInfoDto;
import com.example.tripDuo.dto.UserReviewDto;
import com.example.tripDuo.entity.UserProfileInfo;
import com.example.tripDuo.enums.FollowType;
import com.example.tripDuo.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<UserDto>> users_List() {
		return ResponseEntity.ok(userService.getUserList());
	}
	
	@GetMapping("/profile-info")
	public ResponseEntity<List<UserProfileInfo>> userProfileInfoList() {
		return ResponseEntity.ok(userService.getUserProfileInfoList());
	}
	

	@GetMapping("/{id:[0-9]+}")
	public ResponseEntity<Map<String, Object>> getUserProfileInfoById(@PathVariable Long id) {

		Map<String, Object> userDatas = userService.getUserProfileInfoById(id);
		
		if (userDatas != null) {
			return ResponseEntity.ok(userDatas);
		} else {
			return ResponseEntity.notFound().build(); // 유저가 없으면 404 반환
		}
	}

	@GetMapping("/username/{username:[a-z0-9]+}")
	public ResponseEntity<UserProfileInfoDto> getUserProfileInfoByUsername(@PathVariable("username") String username) {
		UserProfileInfoDto userProfileInfoDto = userService.getUserProfileInfoByUsername(username);
		if (userProfileInfoDto != null) {
			return ResponseEntity.ok(userProfileInfoDto);
		} else {
			return ResponseEntity.notFound().build(); // 유저가 없으면 404 반환
		}
	}

	@PostMapping("/check/{checkType}")
	public ResponseEntity<Boolean> checkUser(@PathVariable("checkType") String checkType,
			@RequestBody String checkString) {
		if (checkType == "nickname") {
			String nickname = URLDecoder.decode(checkString, StandardCharsets.UTF_8);
			checkString = nickname;
		}
		return ResponseEntity.ok(userService.checkExists(checkType, checkString));
	}

	@PutMapping("/{id}/profile-info")
	public ResponseEntity<?> updateUserProfileInfo(@PathVariable Long id,
			@RequestParam(required = false) MultipartFile profileImgForUpload, UserProfileInfoDto userProfileInfoDto) {
		// 사용자 정보 업데이트
		return ResponseEntity.ok(userService.updateUserProfileInfo(userProfileInfoDto, profileImgForUpload));
	}	

	@PutMapping("/{id}/change-password")
	public ResponseEntity<Boolean> updateUserPassword(@PathVariable Long id, @RequestBody UserDto userDto) {
		userDto.setId(id);
		// 사용자 비밀번호 업데이트
		return ResponseEntity.ok(userService.updateUserPassword(userDto));
	}

	@PostMapping("/{id}/reset-password")
	public ResponseEntity<Boolean> resetUserPassword(@PathVariable Long id, @RequestBody UserDto userDto) {
		userDto.setId(id);
		return ResponseEntity.ok(userService.resetUserPassword(userDto));
	}

	@PutMapping("/{id}/private-info")
	public ResponseEntity<?> updateUserPrivateInfo(@PathVariable Long id, @RequestBody UserDto userDto) {
		userDto.setId(id);
		userService.updateUserPrivateInfo(userDto);
		return ResponseEntity.ok("User private info updated successfully");
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok(id + " user is deleted");
	}

	// ### follow ###
	// 팔로우/차단 관련 메소드

	// 어떤 유저(userId)의 팔로워/팔로이 리스트를 가져오는 메소드
	@GetMapping("/{userId}/followInfos")
	public ResponseEntity<Map<String, Object>> getFollowInfo(@PathVariable("userId") Long userId) {
		return ResponseEntity.ok(userService.getFollowInfo(userId));
	}

	// 어떤 유저(userId)가 차단한 유저정보 리스트를 가져오는 메소드
	@GetMapping("/{userId}/blockInfos")
	public ResponseEntity<List<UserProfileInfoDto>> getBlockedUserProfileInfo(@PathVariable("userId") Long userId) {
		return ResponseEntity.ok(userService.getBlockInfo(userId));
	}

	// 어떤 유저(followerUserId)가 다른 유저(followeeUserId)를 팔로우/차단(followType) 하기
	@PostMapping("/{followeeUserId}/{followType}/{followerUserId}")
	public ResponseEntity<String> addFollowOrBlock(@PathVariable("followeeUserId") Long followeeUserId,
	        @PathVariable("followType") String followType,
	        @PathVariable("followerUserId") Long followerUserId) {
		
		FollowType followTypeEnum;
		
		try {
			followTypeEnum = FollowType.fromString(followType);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body("Invalid follow type: " + followType);
		}
		
		UserFollowDto userFollowDto = new UserFollowDto();
		userFollowDto.setFolloweeUserId(followeeUserId);
		userFollowDto.setFollowType(followTypeEnum);
		userFollowDto.setFollowerUserId(followerUserId);
		
		userService.addFollowOrBlock(userFollowDto);
		
		return ResponseEntity.ok(followType + "ed successfully");
	}
	
	// 어떤 유저(followerUserId)가 다른 유저(followeeUserId)를 팔로우/차단(followType) 해제하기
	@DeleteMapping("/{followeeUserId}/{followType}/{followerUserId}")
	public ResponseEntity<String> deleteFollowOrBlock(@PathVariable("followeeUserId") Long followeeUserId,
	        @PathVariable("followType") String followType,
	        @PathVariable("followerUserId") Long followerUserId) {
		
		FollowType followTypeEnum;

		try {
			followTypeEnum = FollowType.fromString(followType);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body("Invalid follow type: " + followType);
		}
		
		UserFollowDto userFollowDto = new UserFollowDto();
		userFollowDto.setFolloweeUserId(followeeUserId);
		userFollowDto.setFollowType(followTypeEnum);
		userFollowDto.setFollowerUserId(followerUserId);
		
		userService.deleteFollowOrBlock(userFollowDto);
		
		return ResponseEntity.ok("un" + followType + "ed successfully");
	}

	// ### review ###
	// 리뷰 관련 메소드

	// 어떤 유저(reviewerId)가 다른 유저(revieweeId)를 대상으로 리뷰(review)를 작성하기
	@PostMapping("/{revieweeId}/review/{reviewerId}")
	public ResponseEntity<UserReviewDto> writeReview(@PathVariable("revieweeId") Long revieweeId,
			@PathVariable("reviewerId") Long reviewerId,
			@RequestBody UserReviewDto userReviewDto) {
		
		userReviewDto.setRevieweeId(revieweeId);
		userReviewDto.setReviewerId(reviewerId);
		UserReviewDto insertedUserReviewDto = userService.writeReview(userReviewDto);
		return ResponseEntity.ok(insertedUserReviewDto);
	}

	// 어떤 유저(reviewerId)가 다른 유저(revieweeId)를 대상으로 작성한 리뷰(review)를 수정하기
	@PutMapping("/{revieweeId}/review/{reviewerId}")
	public ResponseEntity<UserReviewDto> updateReview(@PathVariable("revieweeId") Long revieweeId,
			@PathVariable("reviewerId") Long reviewerId,
			@RequestBody UserReviewDto userReviewDto) {
		
		userReviewDto.setRevieweeId(revieweeId);
		userReviewDto.setReviewerId(reviewerId);
		UserReviewDto updatedUserReivewDto = userService.updateReview(userReviewDto);
		return ResponseEntity.ok(updatedUserReivewDto);
	}

	// 어떤 유저(reviewerId)가 다른 유저(revieweeId)를 대상으로 작성한 리뷰(review)를 삭제하기
	@DeleteMapping("/{revieweeId}/review/{reviewerId}")
	public ResponseEntity<String> deleteReview(@PathVariable("revieweeId") Long revieweeId,
			@PathVariable("reviewerId") Long reviewerId) {
		
		userService.deleteReview(revieweeId, reviewerId);
		return ResponseEntity.ok("Review deleted successfully");
	}
}