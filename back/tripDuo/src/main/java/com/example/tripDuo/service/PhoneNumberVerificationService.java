package com.example.tripDuo.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * 휴대폰 번호로 인증번호를 전송하는 class
 * 해시한 6자리 문자열을 전송
 * TODO : 숫자 6자리로 변경
 */
@Service
public class PhoneNumberVerificationService {
	
	private final ConcurrentHashMap<String, VerificationData> verificationMap = new ConcurrentHashMap<>();
	private final SecureRandom secureRandom = new SecureRandom();
	private static final int CODE_VALIDITY_DURATION = 1; // in minutes

	public String generateVerificationCode() {
		byte[] randomBytes = new byte[4]; // 6자리 숫자를 만들기 위해 4바이트 사용
		secureRandom.nextBytes(randomBytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).substring(0, 6);
	}

	public void storeVerificationCode(String phoneNumber, String code) {
		String hashedCode = hashCode(code);
		
		verificationMap.put(phoneNumber, new VerificationData(hashedCode, LocalDateTime.now()));
	}

	public boolean verifyCode(String phoneNumber, String code) {
		VerificationData data = verificationMap.get(phoneNumber);
//		### test code
//		System.out.println("[PhoneNumberVerificationService] verifyCode : " + phoneNumber + " ::: " + code);
//		System.out.println(data); // data가 현재 null임 
//		
//		for (Map.Entry<String, VerificationData> entry : verificationMap.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue().getHashedCode());
//        }
//		
//		System.out.println(verificationMap.toString());
		
		if (data != null && isCodeValid(data.getTimestamp())) {
			return data.getHashedCode().equals(hashCode(code));
		}
		return false;
	}

	private String hashCode(String code) {
	    try {
	        // SHA-256 해시 알고리즘 인스턴스 생성
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        
	        // 입력 문자열을 해시 처리
	        byte[] encodedHash = digest.digest(code.getBytes(StandardCharsets.UTF_8));
	        
	        // 해시된 바이트 배열을 16진수 문자열로 변환
	        return bytesToHex(encodedHash);
	        
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Error occurred while hashing code", e);
	    }
	}

	private String bytesToHex(byte[] hash) {
	    StringBuilder hexString = new StringBuilder(2 * hash.length);
	    for (byte b : hash) {
	        String hex = Integer.toHexString(0xff & b);
	        if (hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
	
	private boolean isCodeValid(LocalDateTime timestamp) {
		return timestamp.plusMinutes(CODE_VALIDITY_DURATION).isAfter(LocalDateTime.now());
	}

	private static class VerificationData {
		private final String hashedCode;
		private final LocalDateTime timestamp;

		public VerificationData(String hashedCode, LocalDateTime timestamp) {
			this.hashedCode = hashedCode;
			this.timestamp = timestamp;
		}

		public String getHashedCode() {
			return hashedCode;
		}

		public LocalDateTime getTimestamp() {
			return timestamp;
		}
	}
}
