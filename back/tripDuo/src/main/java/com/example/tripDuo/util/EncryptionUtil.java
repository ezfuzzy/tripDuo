package com.example.tripDuo.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    
	@Value("${encrypt.key}")
	private String SECRET_KEY_FOR_ENCRYPT_PHONE_NUMBER;

    // 전화번호 암호화
    public String encrypt(String phoneNumber) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY_FOR_ENCRYPT_PHONE_NUMBER.getBytes(), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(phoneNumber.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // 전화번호 복호화
    public String decrypt(String encryptedPhoneNumber) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY_FOR_ENCRYPT_PHONE_NUMBER.getBytes(), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedPhoneNumber));
        return new String(decrypted);
    }
}