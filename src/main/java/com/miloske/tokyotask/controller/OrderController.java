package com.miloske.tokyotask.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.miloske.tokyotask.entity.Customer;
import com.miloske.tokyotask.entity.Item;
import com.miloske.tokyotask.entity.Order;
import com.miloske.tokyotask.entity.OrderItem;
import com.miloske.tokyotask.entity.OrderStatus;
import com.miloske.tokyotask.repository.CustomerRepository;
import com.miloske.tokyotask.repository.ItemRepository;
import com.miloske.tokyotask.repository.OrderRepository;

@RestController
public class OrderController {

	public OrderController(OrderRepository orderRepository, CustomerRepository customerRepository, ItemRepository itemRepository) {
		this.orderRepository = orderRepository;
		this.customerRepository = customerRepository;
		this.itemRepository = itemRepository;
	}
	
	@PostMapping("/order/create")
	public ResponseEntity<Order> createOrder(@RequestParam Long customerId) {
		final Customer customer = customerRepository.findById(customerId).orElse(null);
		if(customer == null) {
			return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
		}
		
		Order newOrder = new Order(customer);
		customerRepository.save(customer);
		
		return new ResponseEntity<Order>(orderRepository.save(newOrder), HttpStatus.CREATED);
	}
	
	@PutMapping("/order/add/{id}")
	public ResponseEntity<Order> addToOrder(@RequestParam Long customerId, @RequestParam Long itemId, @RequestParam int count, @PathVariable Long id) {
		return orderRepository.findById(id).map(order -> {
			// Make sure that the order belongs to the customer
			if(!authenticateOrder(order, customerId)) {
				return new ResponseEntity<Order>(HttpStatus.UNAUTHORIZED);
			}
			
			final OrderStatus orderStatus = order.getOrderStatus();
			// Don't add items to a submitted order
			if(orderStatus != OrderStatus.SUBMITTED) {
				final Item item = itemRepository.findById(itemId).orElse(null);
				if(item == null) {
					return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
				}
				
				addItems(order, item, count);
				// Make sure the order is changed to pending after adding an item to a empty order
				if(orderStatus == OrderStatus.CREATED) {
					order.setOrderStatus(OrderStatus.PENDING);
				}
				
				// Save modified order to the DB
				orderRepository.save(order);
			}
			
			return new ResponseEntity<Order>(order, HttpStatus.OK);
		}).orElseGet(() -> {
			return new ResponseEntity<Order>(HttpStatus.BAD_REQUEST);
		});
	}
	
	@PutMapping("/order/remove/{id}")
	public ResponseEntity<Order> removeFromOrder(@RequestParam Long customerId, @RequestParam Long itemId, @RequestParam int count, @PathVariable Long id) {
		return orderRepository.findById(id).map(order -> {
			// Make sure that the Order belongs to the customer
			if(!authenticateOrder(order, customerId)) {
				return new ResponseEntity<Order>(HttpStatus.UNAUTHORIZED);
			}
			
			// Don't remove items from submitted or empty orders
			if(order.getOrderStatus() == OrderStatus.PENDING) {
				final Item item = itemRepository.findById(itemId).orElse(null);
				if(item == null) {
					return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
				}
				
				removeItems(order, item, count);
				// If the last item from an order is removed set it as created
				if(order.getItems().isEmpty()) {
					order.setOrderStatus(OrderStatus.CREATED);
				}
				
				// Save modified order to the DB
				orderRepository.save(order);
			}
			
			return new ResponseEntity<Order>(order, HttpStatus.OK);
		}).orElseGet(() -> {
			return new ResponseEntity<Order>(HttpStatus.BAD_REQUEST);
		});
	}
	
	@DeleteMapping("/order/delete/{id}")
	public ResponseEntity<String> deleteOrder(@RequestParam Long customerId, @PathVariable Long id) {
		return orderRepository.findById(id).map(order -> {
			if(!authenticateOrder(order, customerId)) {
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			}
			
			orderRepository.delete(order);
			return new ResponseEntity<String>(String.format("Order %d deleted", id), HttpStatus.OK);
		}).orElseGet(() -> {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		});
	}
	
	@PutMapping("/order/submit/{id}")
	public ResponseEntity<Order> submitOrder(@RequestParam Long customerId, @PathVariable Long id) {
		return orderRepository.findById(id).map(order -> {
			if(!authenticateOrder(order, customerId)) {
				return new ResponseEntity<Order>(HttpStatus.UNAUTHORIZED);
			}
			
			// No reason to submit an empty or submitted order (especially if it requires further processing)
			if(order.getOrderStatus() != OrderStatus.PENDING) {
				return new ResponseEntity<Order>(HttpStatus.BAD_REQUEST);
			}
			
			order.setOrderStatus(OrderStatus.SUBMITTED);
			return new ResponseEntity<Order>(orderRepository.save(order), HttpStatus.OK);
		}).orElseGet(() -> {
			return new ResponseEntity<Order>(HttpStatus.BAD_REQUEST);
		});
	}
	
	private boolean authenticateOrder(Order order, Long customerId) {
		return order.getCustomer().getId() == customerId;
	}
	
	private void addItems(Order order, Item item, int count) {
		if(count <= 0) {
			throw new IllegalArgumentException();
		}
		
		OrderItem targetItem = findByItem(order, item);
		if(targetItem == null) {
			OrderItem orderItem = new OrderItem(count, order, item);
			order.getItems().add(orderItem);
		} else {
			targetItem.addQuantity(count);
		}
		
		order.addToTotal(item.getPrice(), count);
	}
	
	private void removeItems(Order order, Item item, int count) {
		if(count <= 0) {
			throw new IllegalArgumentException();
		}
		
		OrderItem targetItem = findByItem(order, item);
		if(targetItem == null) {
			return;
		}
		
		int countToRemove = Math.min(targetItem.getQuantity(), count);
		targetItem.addQuantity(-countToRemove);
		
		order.addToTotal(-item.getPrice(), countToRemove);
		
		if(targetItem.getQuantity() == 0) {
			order.getItems().remove(targetItem);
		}
	}
	
	private static OrderItem findByItem(Order order, Item item) {
		for(OrderItem orderItem : order.getItems()) {
			if(orderItem.getItem().equals(item)) {
				return orderItem;
			}
		}
		
		return null;
	}
	
	private final OrderRepository orderRepository; 
	
	private final CustomerRepository customerRepository;
	
	private final ItemRepository itemRepository;
}
