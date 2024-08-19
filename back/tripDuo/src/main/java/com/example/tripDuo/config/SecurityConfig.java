package com.example.tripDuo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean // 메소드에서 return해주는 객체를 Bean으로 만들어줌
	public SecurityFilterChain securityFilerChain(HttpSecurity httpSecurity) throws Exception {
	    
	    String[] whiteList = { 
		  	"/", "/error", 
	
		  	"/user/loginform", "/user/login_fail", 
		  	"/user/signup_form", "/user/signup"
	
	    };
	
		httpSecurity
		.headers((header) -> {
			header.frameOptions(option -> option.sameOrigin());   
		})
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests((config) -> {
		  config
		  	.requestMatchers(whiteList).permitAll()
		  	.requestMatchers("/admin/**").hasRole("ADMIN")
		.requestMatchers("/staff/**").hasAnyRole("ADMIN", "STAFF")
		  	.anyRequest().authenticated();
		})
		.sessionManagement((config) -> {
		  config.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		});
		// 	.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
	    return httpSecurity.build();
	}
	  
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// authenticationManager
}
