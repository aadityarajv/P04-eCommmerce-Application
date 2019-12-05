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
		log.info("Order submit method invoked by username: {}", username);
		User user = userRepository.findByUsername(username);
		if(user != null) {
			UserOrder order = UserOrder.createFromCart(user.getCart());
			orderRepository.save(order);
			log.info("Order submitted successfully.");
			return ResponseEntity.ok(order);
		} else {
		    log.error("Uhoh, Unable to find this username: {}", username);
		    return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		log.info("Get all order submitted history for this username method invoked: {}", username);
		User user = userRepository.findByUsername(username);
		if(user != null) {
			log.info("Get orders history for user method success");
			return ResponseEntity.ok(orderRepository.findByUser(user));
		} else {
		log.error("Uhoh, Unable to find this username: {}", username);
		return ResponseEntity.notFound().build();
		}
	}
}
