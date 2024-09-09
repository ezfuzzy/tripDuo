package com.example.tripDuo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tripDuo.dto.PostDto;
import com.example.tripDuo.entity.Post;
import com.example.tripDuo.repository.PostRepository;

@Service
public class PostServiceImpl implements PostService {

	private final PostRepository postRepo;

	public PostServiceImpl(PostRepository postRepo) {
		this.postRepo = postRepo;
	}
	
	@Override
	public List<PostDto> getPostList(String type) {
		// type별 get list 
		return postRepo.findByTypeOrderByIdDesc(type).stream().map(PostDto::toDto).toList();
	}

	@Override
	public PostDto getPostById(Long id) {
		return PostDto.toDto(postRepo.findById(id).get());
	}

	@Override
	public void writePost(PostDto dto) {
		postRepo.save(Post.toEntity(dto));
	}
	
	@Override
	public PostDto updatePost(PostDto dto) {
		
		// put mapping이니까 정보 삭제 안되게 ... 
		
		// 1. 만약 기존의 모든 정보가 그대로 넘어오면
		postRepo.save(Post.toEntity(dto));
		
		// 2. 만약 기존 정보중 일부만 넘어오면 -> controller에서 setId하고 넘겨준 다음에 로직 실행
		// 협의하고 코드 짤 부분
		
		return dto; // 정보가 다 들어있는 dto 반환 - 안해줘도 되긴함
	}

	@Override
	public void deletePost(Long id) {
		postRepo.deleteById(id);
	}

}
