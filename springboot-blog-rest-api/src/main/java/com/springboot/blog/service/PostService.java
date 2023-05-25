package com.springboot.blog.service;

import java.util.List;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;

public interface PostService {
	PostDto createPost(PostDto postdto);

	PostResponse getAllPosts(int pageNo, int pageSize, String sortBt, String sortDir);

	PostDto getPostById(long id);

	PostDto updatePost(PostDto dto, long id);

	void deleteById(long id);
}
