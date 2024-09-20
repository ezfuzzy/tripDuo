package com.example.tripDuo.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

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

	@GetMapping("/{id:[0-9]+}")
	public ResponseEntity<UserProfileInfoDto> getUserProfileInfoById(@PathVariable Long id) {

		UserProfileInfoDto userProfileInfoDto = userService.getUserProfileInfoById(id);
		
		if (userProfileInfoDto != null) {
			return ResponseEntity.ok(userProfileInfoDto);
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

	@PutMapping("/{id}")
	public ResponseEntity<UserProfileInfoDto> updateUserInfo(@PathVariable Long id,
			@RequestParam(required = false) MultipartFile profileImgForUpload, UserProfileInfoDto dto) {
		// 사용자 정보 업데이트
		userService.updateUserProfileInfo(dto, profileImgForUpload);

		return ResponseEntity.ok(dto);
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

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok(id + " user is deleted");
	}

	// ### follow ###
	@GetMapping("/{followeeUserId}/followersInfo")
	public ResponseEntity<List<UserProfileInfoDto>> getFollowerProfileInfoList(@PathVariable("followeeUserId") Long followeeUserId) {
		
		List<UserProfileInfoDto> followerProfileInfoList = userService.getFollowerProfileInfoList(followeeUserId);
		return ResponseEntity.ok(followerProfileInfoList);
	}

	@GetMapping("/{followerUserId}/{followType}/followeesInfo")
	public ResponseEntity<List<UserProfileInfoDto>> getFolloweeProfileInfoList(@PathVariable("followerUserId") Long followerUserId,
			@PathVariable("followType") String followType) {
		
		FollowType followTypeEnum;
		
		try {
			followTypeEnum = FollowType.fromString(followType);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Collections.emptyList());
		}
		
		List<UserProfileInfoDto> followeeProfileInfoList = userService.getFolloweeProfileInfoList(followerUserId, followTypeEnum);
		return ResponseEntity.ok(followeeProfileInfoList);
	}

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

	@PostMapping("/{id}/reviews")
	public ResponseEntity<String> writeReview(@PathVariable Long id, @RequestBody UserReviewDto dto) {
		dto.setRevieweeId(id);
		userService.addReview(dto);
		return ResponseEntity.ok("Review added successfully");
	}

	@PutMapping("/{id}/reviews")
	public ResponseEntity<String> updateReview(@PathVariable Long id, @RequestBody UserReviewDto dto) {
		dto.setId(id);
		userService.updateReview(dto);
		return ResponseEntity.ok("Review updated successfully");
	}

	@DeleteMapping("/{id}/reviews")
	public ResponseEntity<String> deleteReview(@PathVariable Long id) {
		userService.deleteReview(id);
		return ResponseEntity.ok("Review deleted successfully");
	}

}