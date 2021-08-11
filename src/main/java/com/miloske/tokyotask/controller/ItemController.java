package com.miloske.tokyotask.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.miloske.tokyotask.entity.Item;
import com.miloske.tokyotask.repository.ItemRepository;

@RestController
public class ItemController {

	public ItemController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}
	
	@GetMapping("/item/get/{id}")
	public ResponseEntity<Item> getItem(@PathVariable Long id) {
		return itemRepository.findById(id).map(item -> {
			return new ResponseEntity<Item>(item, HttpStatus.OK);
		}).orElseGet(() -> {
			return new ResponseEntity<Item>(HttpStatus.BAD_REQUEST);
		});
	}
	
	@GetMapping("/item/getAll")
	public ResponseEntity<Iterable<Item>> getAllItems() {
		return new ResponseEntity<Iterable<Item>>(itemRepository.findAll(), HttpStatus.OK);
	}
	
	private ItemRepository itemRepository;
}
