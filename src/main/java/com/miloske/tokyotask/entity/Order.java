package com.miloske.tokyotask.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "orders")
public class Order {

	public Order(Customer customer) {
		this.customer = customer;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
        this.id = id;
    }

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
	
	public double getTotal() {
		return total;
	}
	
	public void addToTotal(double price, int count) {
		total += price * count;
	}
	
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		StringBuilder itemsStringBuilder = new StringBuilder();
		for(OrderItem item : items) {
			itemsStringBuilder.append(item.toString()).append('\n');
		}
		
		return String.format("Order [id=%s, customer=%s, items=%s]", id, customer, itemsStringBuilder.toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	protected Order() { }

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> items = new ArrayList<>();
	
	double total = 0;
	
	@Enumerated(EnumType.ORDINAL)
	private OrderStatus orderStatus = OrderStatus.CREATED;
	
	@ManyToOne
	private Customer customer;
}
