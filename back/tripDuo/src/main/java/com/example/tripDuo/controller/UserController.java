package com.example.tripDuo.controller;

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
	
	@GetMapping("/")
	public String users_List() {
		return "users list";
	}
	
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable int id) {
        // 사용자 정보 조회
        UserDto user = service.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable("username") String username) {
    	System.out.println("####");
    	System.out.println(username);
    	// 사용자 정보 조회
    	UserDto user = service.getUserByUsername(username);
    	user.setPassword("응 비번 없어");
    	
    	return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable int id,@RequestParam(required = false) MultipartFile profileImgForUpload, UserDto userDto) {
        // 사용자 정보 업데이트
       service.updateUser(userDto, profileImgForUpload);
       
       return ResponseEntity.ok(userDto);
    }

    

    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        service.deleteUser(id);
    	return ResponseEntity.ok(id + " user is deleted");
    }
	
}
