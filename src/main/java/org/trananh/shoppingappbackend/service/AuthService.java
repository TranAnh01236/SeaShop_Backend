package org.trananh.shoppingappbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.trananh.shoppingappbackend.model.User;
import org.trananh.shoppingappbackend.repository.UserRepository;

@Service
public class AuthService {
	
	@Autowired(required = true)
	private JwtTokenProvider jwtTokenProvider;
	
	public User verifyToken(String token) {
		try {
			token = token.substring(7);
		} catch (Exception e) {
			return null;
		}
		if (token == null || token.equals("")) {
			return null;
		}
		User user = jwtTokenProvider.getUserIdFromJWT(token);
		return user;
	}
	
	public String generateToken(String userId) {
		return jwtTokenProvider.generateToken(userId);
	}
	
}
