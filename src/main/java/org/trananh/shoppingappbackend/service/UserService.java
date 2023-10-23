package org.trananh.shoppingappbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trananh.shoppingappbackend.model.User;
import org.trananh.shoppingappbackend.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public UserService() {
		
	}
	
	public User findById(String id) {
		return userRepository.findById(id).orElse(null);
	}
	
}
