package com.leewatterson.events.services;

import org.springframework.stereotype.Service;

import com.leewatterson.events.models.Comment;
import com.leewatterson.events.repositories.CommentRepository;

@Service
public class CommentService {
	private final CommentRepository commentRepository;
	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	public Comment createComment(Comment comment) {
		return commentRepository.save(comment);
	}
}
