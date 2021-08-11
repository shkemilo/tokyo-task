package com.miloske.tokyotask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.miloske.tokyotask.entity.Customer;
import com.miloske.tokyotask.entity.Item;
import com.miloske.tokyotask.repository.CustomerRepository;
import com.miloske.tokyotask.repository.ItemRepository;

@Component
public class DatabaseLoader {

	private static final Logger log = LoggerFactory.getLogger(DatabaseLoader.class);
	
	@Bean
	public CommandLineRunner initDatabase(CustomerRepository customerRepository, ItemRepository itemRepository) {
	    return (args) -> {
	    	log.info("Database initialization started.");
	    	
	    	final Customer[] customers = {
	    		new Customer("Amalija", "Pecora", "amalija.pecora@test.com", "123456"),
	    		new Customer("Lutif", "Philippe", "lutif.philippe@test.com", "234567"),
	    		new Customer("Mara", "Olmos", "mara.olmos@test.com", "345678"),
	    		new Customer("Padma", "Kaufer", "padma.kaufer@test.com", "456789"),
	    		new Customer("Carey", "Hase", "carey.hase@test.com", "567890")
	    	};
	    	
	    	log.info("Loading customers started.");
	    	for(Customer customer : customers) {
	    		customerRepository.save(customer);
	    	}
	    	log.info("Loading customers finished.");
	    	
	    	final Item[] items = {
	    			new Item("Plazma", "Best biscuit in the world!", 120, "itemImages/plazma.jpg"),
	    			new Item("Charger", "Buy it, because you will lose your exsisting one!", 3000, "itemImages/charger.jpg"),
	    			new Item("Monitor", "Why not just add a 4th one while we're at it?!", 25000, "itemImages/monitor.jpg"),
	    			new Item("Gum Piece", "How else would we give you back your small change?", 10, "itemImages/gumpiece.jpg"),
	    			new Item("Burek", "It's burek, does it need a description?", 200, "itemImages/burek.jpg")
	    	};
	    	
	    	log.info("Loading items started.");
	    	for(Item item : items) {
	    		itemRepository.save(item);
	    	}
	    	log.info("Loading items finished.");
	    	
	    	log.info("Database initialization finished.");
	    };
	}
}
