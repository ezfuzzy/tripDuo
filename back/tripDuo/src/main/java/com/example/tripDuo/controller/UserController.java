package com.example.tripDuo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.tripDuo.dto.UserDto;
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
      return ResponseEntity.ok(userService.checkExists(checkType, checkString));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserInfo(@PathVariable Long id,@RequestParam(required = false) MultipartFile profileImgForUpload, UserDto userDto) {
        // 사용자 정보 업데이트
       userService.updateUserInfo(userDto, profileImgForUpload);
       
       return ResponseEntity.ok(userDto);
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
	
}
