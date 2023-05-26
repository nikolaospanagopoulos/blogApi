package com.springboot.blog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFound;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;

@Service
public class CommentServiceImpl implements CommentService {

	private CommentRepository repository;
	private PostRepository postRepository;

	public CommentServiceImpl(CommentRepository repository, PostRepository postRepository) {
		super();
		this.repository = repository;
		this.postRepository = postRepository;
	}

	private CommentDto mapToDto(Comment comment) {
		CommentDto dto = new CommentDto(comment.getId(), comment.getName(), comment.getEmail(), comment.getBody());
		return dto;
	}

	private Comment mapToEntity(CommentDto dto) {
		Comment comment = new Comment(dto.getId(), dto.getName(), dto.getEmail(), dto.getBody());
		return comment;
	}

	@Override
	public CommentDto createComment(long postId, CommentDto commentDto) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFound("Post", "id", Long.toString(postId)));

		Comment comment = mapToEntity(commentDto);

		comment.setPost(post);

		return mapToDto(repository.save(comment));
	}

	public CommentRepository getRepository() {
		return repository;
	}

	public void setRepository(CommentRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<CommentDto> getCommentsByPostId(long postId) {
		postRepository.findById(postId).orElseThrow(() -> new ResourceNotFound("Post", "id", Long.toString(postId)));

		List<CommentDto> commentsDto = repository.findByPostId(postId).stream().map(comment -> mapToDto(comment))
				.collect(Collectors.toList());
		return commentsDto;
	}

}
