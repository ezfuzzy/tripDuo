package com.example.tripDuo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tripDuo.dto.PostDto;
import com.example.tripDuo.entity.Post;
import com.example.tripDuo.repository.PostRepository;

@Service
public class PostServiceImpl implements PostService{

	@Autowired
	private PostRepository postRepo;
	
	@Override
	public List<PostDto> getPostList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PostDto getPostById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writePost(PostDto dto) {
		postRepo.save(Post.toEntity(dto));
		
	}
	
	@Override
	public Boolean updatePost(PostDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletePost(Long id) {
		// TODO Auto-generated method stub
		
	}

}
