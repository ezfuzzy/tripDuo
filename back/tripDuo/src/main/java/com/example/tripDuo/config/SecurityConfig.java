package com.example.tripDuo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.tripDuo.filter.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${jwt.name}")
	private String jwtName;

	@Autowired
	private JwtFilter jwtFilter;

	@Bean
	public SecurityFilterChain securityFilerChain(HttpSecurity httpSecurity) throws Exception {

		String[] whiteList = { "/", "/home", "/api/**", };

		httpSecurity.headers((header) -> {
			header.frameOptions(option -> option.sameOrigin());
		}).csrf(csrf -> csrf.disable()).authorizeHttpRequests((config) -> {
			config.requestMatchers(whiteList).permitAll() // whiteList
					.requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/staff/**")
					.hasAnyRole("ADMIN", "STAFF").anyRequest().authenticated();
		}).sessionManagement((config) -> {
			config.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
			UserDetailsService userDetailService) throws Exception {

		return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userDetailService)
				.passwordEncoder(bCryptPasswordEncoder).and().build();
	}
}
