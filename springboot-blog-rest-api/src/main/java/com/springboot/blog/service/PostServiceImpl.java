package com.springboot.blog.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFound;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import org.springframework.data.domain.Pageable;

@Service
public class PostServiceImpl implements PostService {
	private PostRepository repository;

	public PostServiceImpl(PostRepository repository) {
		this.repository = repository;
	}

	public PostDto createPost(PostDto postdto) {

		Post post = mapPost(postdto);

		Post newPost = repository.save(post);

		return mapToDTO(newPost);
	}

	private Post mapPost(PostDto postdto) {
		Post post = new Post();
		post.setTitle(postdto.getTitle());
		post.setDescription(postdto.getDescription());
		post.setContent(postdto.getContent());
		return post;
	}

	@Override
	public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy)
				: Sort.by(sortBy).descending();

		Pageable page = PageRequest.of(pageNo, pageSize, sort);

		Page<Post> posts = repository.findAll(page);

		List<Post> postContent = posts.getContent();

		List<PostDto> allPosts = postContent.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

		PostResponse postResponse = new PostResponse(allPosts, posts.getNumber(), posts.getSize(),
				posts.getTotalElements(), posts.getTotalPages(), posts.isLast());

		return postResponse;
	}

	private PostDto mapToDTO(Post post) {
		PostDto postDto = new PostDto();
		postDto.setId(post.getId());
		postDto.setDescription(post.getDescription());
		postDto.setContent(post.getContent());
		postDto.setTitle(post.getTitle());
		return postDto;
	}

	@Override
	public PostDto getPostById(long id) {
		Post post = repository.findById(id).orElseThrow(() -> new ResourceNotFound("Post", "id", Long.toString(id)));
		return mapToDTO(post);
	}

	@Override
	public PostDto updatePost(PostDto dto, long id) {
		Post post = repository.findById(id).orElseThrow(() -> new ResourceNotFound("Post", "id", Long.toString(id)));
		post.setDescription(dto.getDescription());
		post.setContent(dto.getContent());
		post.setTitle(dto.getTitle());

		return mapToDTO(repository.save(post));

	}

	@Override
	public void deleteById(long id) {
		Post post = repository.findById(id).orElseThrow(() -> new ResourceNotFound("Post", "id", Long.toString(id)));
		repository.delete(post);

	}
}
