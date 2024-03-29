package com.example.demo.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		log.info("Invoked, Find userById method: {}", id);
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		log.info("Invoked, Find userByUsername method: {}", username);
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("Failed!!!, Unable to find username: {}", username);
			return ResponseEntity.notFound().build();
		} else {
			log.info("Success, Username found: {}", username);
			return ResponseEntity.ok(user);
		}
//		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		log.info("Invoked, Create user method.");
		User user = new User();

		if(createUserRequest.getUsername() == null || createUserRequest.getPassword() == null || createUserRequest.getConfirmPassword() == null){
			log.error("Failed!!!, The username or password field is empty!!");
			return ResponseEntity.badRequest().build();
		}

		if(!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			log.error("Failed!!!, Error with the password, password did not match!!");
			return ResponseEntity.badRequest().build();
		}

		if(createUserRequest.getConfirmPassword().length() < 7){
			log.error("Failed!!!, Cannot create user, Password too short!!: {}", createUserRequest.getPassword().length());
			return ResponseEntity.badRequest().build();
		}

		user.setUsername(createUserRequest.getUsername());
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

		log.info(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);

		userRepository.save(user);
		log.info("Success, User created successfully");
		return ResponseEntity.ok(user);
	}
	
}
