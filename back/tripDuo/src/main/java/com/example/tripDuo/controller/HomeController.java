package com.example.tripDuo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class HomeController {

	
	/**
	 * 여기 test 용도 컨트롤러 입니다
	 *  !!! 절대 참고 금지 !!!  
	 */

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@ResponseBody
	@PostMapping("/test/test2")
	public String test2(@RequestBody String oldPasswordWithNewPassword) {
		try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(oldPasswordWithNewPassword);

            String username = jsonNode.get("username").asText();
            String oldPassword = jsonNode.get("oldPassword").asText();
            String newPassword = jsonNode.get("newPassword").asText();

            
            System.out.println("username: " + username);
            System.out.println("oldPassword: " + oldPassword);
            System.out.println("newPassword: " + newPassword);
            
            UserDto dto = UserDto.toDto(userRepo.findByUsername(username));

            if (!encoder.matches(oldPassword, dto.getPassword())) {
                throw new Exception("기존 비밀번호가 일치하지 않습니다.");
            }
            String encodedNewPassword = encoder.encode(newPassword);
            dto.setPassword(encodedNewPassword);
    		userRepo.save(User.toEntity(dto));

    		
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
		return "true";
	}
	
	@ResponseBody
	@GetMapping("/test/test3")
	public String test3() {
		return "ehlsmsep";
	}
	
}
