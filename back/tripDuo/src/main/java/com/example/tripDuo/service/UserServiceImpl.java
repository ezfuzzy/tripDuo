package com.example.tripDuo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.tripDuo.dto.UserDto;
import com.example.tripDuo.entity.User;
import com.example.tripDuo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Value("${file.location}")
	private String fileLocation;

	@Autowired
	private UserRepository repo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<UserDto> getUserList() {
		return repo.findAll().stream().map(UserDto::toDto).toList();
	}
	
	@Override
	public UserDto getUserById(Long id) {
		return UserDto.toDto(repo.findById(id).get());
	}

	@Override
	public UserDto getUserByUsername(String username) {
		return UserDto.toDto(repo.findByUsername(username));
	}

	@Override
	public UserDto getUserByPhoneNumber(String phoneNumber) {
		return UserDto.toDto(repo.findByPhoneNumber(phoneNumber));
	}

	@Override
	public UserDto getUserByEmail(String email) {
		return UserDto.toDto(repo.findByEmail(email));
	}

	@Override
	public Boolean checkExists(String checkType, String checkString) {
		return switch (checkType) {
			case "username" -> repo.existsByUsername(checkString);
			case "nickname" -> repo.existsByNickname(checkString);
			case "phoneNumber" -> repo.existsByPhoneNumber(checkString);
			case "email" -> repo.existsByEmail(checkString);
			default -> throw new IllegalArgumentException("잘못된 체크 타입입니다: " + checkType);
		};
	}

	@Override
	public void updateUserInfo(UserDto dto, MultipartFile profileImgForUpload) {

	    // 프로필 이미지가 존재하고, 파일 크기가 0이 아닌 경우 처리
	    if (profileImgForUpload != null && !profileImgForUpload.isEmpty()) {
	        String saveFileName = UUID.randomUUID().toString();
	        String filePath = fileLocation + File.separator + saveFileName;
	        System.out.println(filePath);

	        // 파일을 지정된 경로로 저장
	        try {
	            File file = new File(filePath);
	            profileImgForUpload.transferTo(file);
	            dto.setProfilePicture(saveFileName);
	        } catch (IOException e) {
	            e.printStackTrace();
	            // throw new CustomFileUploadException("File upload failed", e);
	        }
	    }

	    // 기존 유저 정보 가져오기
		UserDto existingUser = UserDto.toDto(repo.findById(dto.getId()).get());

	    // 비밀번호는 기존 것을 유지
	    dto.setPassword(existingUser.getPassword());

	    // 변경된 정보를 저장
	    repo.save(User.toEntity(dto));

	}

	@Override
	public Boolean updateUserPassword(UserDto dto) {

		if(!dto.getNewPassword().equals(dto.getNewConfirmPassword())) {
			return false;
		}

		UserDto existingUser = UserDto.toDto(repo.findByUsername(dto.getUsername()));
		// 입력된 비밀번호(dto.getPassword())와 기존 비밀번호(existingUser.getPassword()) 비교
		if (!passwordEncoder.matches(existingUser.getPassword(), dto.getPassword())) {
			return false;
			//throw new Exception("기존 비밀번호가 일치하지 않습니다.");
		}

		// 새 비밀번호와 새 비밀번호 확인이 일치하는지 확인
		if (!dto.getNewPassword().equals(dto.getNewConfirmPassword())) {
			throw new IllegalArgumentException("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
		}

		String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
		dto.setPassword(encodedNewPassword);
		repo.save(User.toEntity(dto));

		return true;
	}


	@Override
	public void deleteUser(Long id) {
		repo.deleteById(id);
	}


}
