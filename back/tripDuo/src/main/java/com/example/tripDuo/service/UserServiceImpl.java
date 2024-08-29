package com.example.tripDuo.service;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	UserRepository repo;

	@Override
	public UserDto getUserById(int id) {
		return UserDto.toDto(repo.findById(id).get());
	}

	@Override
	public UserDto getUserByUsername(String username) {
		return UserDto.toDto(repo.findByUsername(username));
	}

	@Override
	public UserDto getUserByPhonenum(String phone_num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUser(UserDto dto) {

		MultipartFile image = dto.getProfileImgForUpload();

		if (image.getSize() != 0) {
			String saveFileName = UUID.randomUUID().toString();
			String filePath = fileLocation + File.separator + saveFileName;

			try {
				File f = new File(filePath);
				dto.getProfileImgForUpload().transferTo(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			dto.setProfilePicture(filePath);
		}

		repo.save(User.toEntity(dto));
	}

	@Override
	public void deleteUser(int id) {
		repo.deleteById(id);
	}

}
