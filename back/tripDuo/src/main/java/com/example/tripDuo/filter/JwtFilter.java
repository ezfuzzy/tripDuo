package com.example.tripDuo.filter;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.tripDuo.service.CustomUserDetailsService;
import com.example.tripDuo.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	@Value("${jwt.name}")
	private String jwtName;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomUserDetailsService service;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 1. extract token from cookie
		Cookie[] cookies = request.getCookies();

		String jwtToken = "";
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (jwtName.equals(cookie.getName())) {
					jwtToken = cookie.getValue();
					break;
				}
			}
		}

		if (jwtToken.equals("")) {
			String authHeader = request.getHeader("Authorization");
			if (authHeader != null) {
				jwtToken = authHeader;
			}
		}

		// 2. get userName from token
		String username = null;
		if (jwtToken.startsWith("Bearer+")) {
			jwtToken = jwtToken.substring(7);
			username = jwtUtil.extractUsername(jwtToken);
		}

		// 3. get UserDetails object from DB
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = service.loadUserByUsername(username);

			// 4. validate token
			boolean isValid = jwtUtil.validateToken(jwtToken, userDetails);

			// 5. if validate: login(spring security를 통과할)
			if (isValid) {
				// 사용자가 제출한 username, pwd와 같은 인증 자격 증명 저장
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// Security context update - 1회성 로그인
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		System.out.println("[tripDuo log] jwt filter on :: " + new Date());

		// 다음 spring 필터 체인 진행하기
		filterChain.doFilter(request, response);
	}
}
