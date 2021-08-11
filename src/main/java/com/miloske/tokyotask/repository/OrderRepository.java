package com.miloske.tokyotask.repository;

import org.springframework.data.repository.CrudRepository;

import com.miloske.tokyotask.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {
	
}
