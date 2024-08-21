package com.example.tripDuo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.repository.UserRepository;
import com.example.tripDuo.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserRepository repo;
	
	
	@Override
	public String login(UserDto dto) throws Exception {
		
		System.out.println(dto.getUsername() + " " + dto.getPassword());

		try {
			UsernamePasswordAuthenticationToken authToken = 
					new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
			
			authManager.authenticate(authToken);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("아이디 혹은 비밀번호가 틀려요!");
		}
		String token = jwtUtil.generateToken(dto.getUsername());
		return "Bearer+" + token;
	}

	@Override
	public String signup(UserDto dto) {
	    
		// 회원가입 페이지에서 user, manager, admin 설정 or admin dashboard page에서 관리
		dto.setRole("USER"); 
		String encodedPwd = encoder.encode(dto.getPassword());
	    dto.setPassword(encodedPwd);
	    
	    repo.save(User.toEntity(dto));
	    
		return "회원가입 완료";
	}

}
