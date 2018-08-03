package com.leewatterson.events.repositories;

import org.springframework.data.repository.CrudRepository;

import com.leewatterson.events.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByEmail(String email);
}
