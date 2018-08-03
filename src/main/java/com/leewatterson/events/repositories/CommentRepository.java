package com.leewatterson.events.repositories;

import org.springframework.data.repository.CrudRepository;

import com.leewatterson.events.models.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {
	
}
