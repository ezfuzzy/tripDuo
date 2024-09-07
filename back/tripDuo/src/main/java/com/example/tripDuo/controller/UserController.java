package com.example.tripDuo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private UserService service;
	
	@GetMapping
	public ResponseEntity<List<UserDto>>users_List() {
		return ResponseEntity.ok(service.getUserList());
	}
	
	@GetMapping("/{id:[0-9]+}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        // 사용자 정보 조회
        UserDto user = service.getUserById(id);
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
    	UserDto user = service.getUserByUsername(username);
    	user.setPassword("응 비번 없어");
    	
    	return ResponseEntity.ok(user);
    }

    @PostMapping("/check/{checkType}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("checkType") String checkType, @RequestBody String checkString) {
      return ResponseEntity.ok(service.checkExists(checkType, checkString));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserInfo(@PathVariable Long id,@RequestParam(required = false) MultipartFile profileImgForUpload, UserDto userDto) {
        // 사용자 정보 업데이트
       service.updateUserInfo(userDto, profileImgForUpload);
       
       return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Boolean> updateUserPassword(@PathVariable int id, @RequestBody UserDto userDto) {
        // 사용자 비밀번호 업데이트
        return ResponseEntity.ok(service.updateUserPassword(userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
    	return ResponseEntity.ok(id + " user is deleted");
    }
	
}
