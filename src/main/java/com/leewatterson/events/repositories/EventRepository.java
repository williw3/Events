package com.leewatterson.events.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.leewatterson.events.models.Event;

public interface EventRepository extends CrudRepository<Event, Long> {
	Event findByName(String name);
	List<Event> findByState(String state);
	List<Event> findByStateNot(String state);
}
