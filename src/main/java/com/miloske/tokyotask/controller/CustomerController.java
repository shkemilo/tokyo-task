package com.miloske.tokyotask.controller;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.miloske.tokyotask.entity.Customer;
import com.miloske.tokyotask.entity.Order;
import com.miloske.tokyotask.repository.CustomerRepository;

@RestController
public class CustomerController {

	public CustomerController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	@GetMapping("customer/get/{id}")
	public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
		final Customer customer = customerRepository.findById(id).orElse(null);
		if(customer == null) {
			return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}
	
	@GetMapping("customer/getOrders/{id}")
	public ResponseEntity<Collection<Order>> getOrders(@PathVariable Long id) {
		final Customer customer = getCustomer(id).getBody();
		if(customer == null) {
			return new ResponseEntity<Collection<Order>>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Collection<Order>>(customer.getOrders(), HttpStatus.OK);
	}
	
	private CustomerRepository customerRepository;
}
