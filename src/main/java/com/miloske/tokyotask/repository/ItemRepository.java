package com.miloske.tokyotask.repository;

import org.springframework.data.repository.CrudRepository;

import com.miloske.tokyotask.entity.Item;

public interface ItemRepository extends CrudRepository<Item, Long>{

}
