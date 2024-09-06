package com.example.tripDuo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,@RequestParam(required = false) MultipartFile profileImgForUpload, UserDto userDto) {
        // 사용자 정보 업데이트
       service.updateUser(userDto, profileImgForUpload);
       
       return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
    	return ResponseEntity.ok(id + " user is deleted");
    }
	
}
