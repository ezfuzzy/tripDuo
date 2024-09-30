package com.example.tripDuo.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.tripDuo.entity.User;
import com.example.tripDuo.entity.UserProfileInfo;
import com.example.tripDuo.repository.UserProfileInfoRepository;
import com.example.tripDuo.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {

	private final UserRepository userRepo;
	private final UserProfileInfoRepository userProfileInfoRepo; 
	
	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expiration;

	@Value("${cloud.aws.cloudfront.profile_picture_url}")
	private String PROFILE_PICTURE_CLOUDFRONT_URL;
	
	public JwtUtil(UserRepository userRepo, UserProfileInfoRepository userProfileInfoRepo) {
		this.userRepo = userRepo;
		this.userProfileInfoRepo = userProfileInfoRepo;
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public String extractIssuer(String token) {
		return extractClaim(token, Claims::getIssuer);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();

		User user = userRepo.findByUsername(username);
		UserProfileInfo userProfileInfo = userProfileInfoRepo.findByUserId(user.getId()); 
		
		claims.put("id", user.getId());
		claims.put("username", user.getUsername());
		claims.put("nickname", userProfileInfo.getNickname());
		claims.put("profilePicture", userProfileInfo.getProfilePicture() != null 
			    ? PROFILE_PICTURE_CLOUDFRONT_URL + userProfileInfo.getProfilePicture() 
			    : null);
		
		return createToken(claims, username);
	}

	private String createToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().
				setClaims(claims)
				.setSubject(subject)
		        .setIssuer("tripDuo.com")            
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		final String issuer = extractIssuer(token);
		
		return (username.equals(userDetails.getUsername()) 
				&& !isTokenExpired(token)
				&& issuer.equals(issuer));
	}
}
