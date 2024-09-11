package com.example.tripDuo.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
import com.example.tripDuo.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
	
	@GetMapping
	public ResponseEntity<List<UserDto>>users_List() {
		return ResponseEntity.ok(userService.getUserList());
	}
	
	@GetMapping("/{id:[0-9]+}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        // 사용자 정보 조회
        UserDto user = userService.getUserById(id);
        user.setPassword("응 비번 없어");
        
        if (user.getId() != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/username/{username:[a-z0-9]+}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable("username") String username) {
    	// 사용자 정보 조회
    	UserDto user = userService.getUserByUsername(username);
    	user.setPassword("응 비번 없어");
    	
    	return ResponseEntity.ok(user);
    }

    @PostMapping("/check/{checkType}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("checkType") String checkType, @RequestBody String checkString) {
    	if(checkType == "nickname") {
    		String nickname = URLDecoder.decode(checkString, StandardCharsets.UTF_8);
    		checkString = nickname;       
    	}
    	return ResponseEntity.ok(userService.checkExists(checkType, checkString));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileInfoDto> updateUserInfo(@PathVariable Long id,@RequestParam(required = false) MultipartFile profileImgForUpload, UserProfileInfoDto dto) {
        // 사용자 정보 업데이트
       userService.updateUserProfileInfo(dto, profileImgForUpload);
       
       return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/password")
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
    
    @PostMapping("/{id}/follows/{type}")
    public ResponseEntity<String> addFollow(@PathVariable("id") Long id, @PathVariable("type") String type, @RequestBody UserFollowDto dto) {
    	dto.setFolloweeUserId(id);
    	dto.setType(type);
    	userService.addFollow(dto);
    	return ResponseEntity.ok("Follow added successfully");
    }
	
    // ### review ###
    
    @PostMapping("/{id}/reviews")
    public ResponseEntity<String> addReview(@PathVariable Long id, @RequestBody UserReviewDto dto){
    	dto.setRevieweeId(id);
    	userService.addReview(dto);
    	return ResponseEntity.ok("Review added successfully");
    }
    
    @DeleteMapping("/{id}/reviews")
    public ResponseEntity<String> deleteReview(@PathVariable Long id){
    	userService.deleteReview(id);
    	return ResponseEntity.ok("Review deleted successfully");
    }
    
    @PutMapping("/{id}/reviews")
    public ResponseEntity<String> updateReview(@PathVariable Long id, @RequestBody UserReviewDto dto){
    	dto.setId(id);
    	userService.editReview(dto);
    	return ResponseEntity.ok("Review updated successfully");
    }
}
