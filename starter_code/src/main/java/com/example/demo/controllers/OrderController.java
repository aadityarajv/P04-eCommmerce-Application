package com.example.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	private static Logger log = LoggerFactory.getLogger(ItemController.class);


	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		log.info("Invoked, Order submitByUsername method: {}", username);
		User user = userRepository.findByUsername(username);
		if(user != null) {
			UserOrder order = UserOrder.createFromCart(user.getCart());
			orderRepository.save(order);
			log.info("Success, Order submitted successfully: {}", username);
			return ResponseEntity.ok(order);
		} else {
			log.error("Failed!!!, Unable to find username: {}", username);
		    return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		log.info("Invoked, Get Order historyByUsername method: {}", username);
		User user = userRepository.findByUsername(username);
		if(user != null) {
			log.info("Success, Get orders history for user method success: {}", username);
			return ResponseEntity.ok(orderRepository.findByUser(user));
		} else {
			log.error("Failed!!!, Unable to find username: {}", username);
		return ResponseEntity.notFound().build();
		}
	}
}
