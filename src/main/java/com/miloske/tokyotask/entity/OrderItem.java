package com.miloske.tokyotask.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "order_items")
public class OrderItem {

	public OrderItem(int quantity, Order order, Item item) {
		this.quantity = quantity;
		this.order = order;
		this.item = item;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
        this.id = id;
    }
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void addQuantity(int count) {
		quantity += count;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	@Override
	public String toString() {
		return String.format("OrderItem [id=%s, quantity=%s, orderId=%d, itemId=%s]", id, quantity, order.getId(), item.getId());
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
		OrderItem other = (OrderItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	protected OrderItem() { }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private int quantity;
	
	@JsonIgnore
	@ManyToOne
	private Order order;
	
	@ManyToOne
	private Item item;
}
