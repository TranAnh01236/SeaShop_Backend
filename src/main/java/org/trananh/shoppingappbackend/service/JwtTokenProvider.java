package org.trananh.shoppingappbackend.service;

import java.util.Collection;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.trananh.shoppingappbackend.model.User;
import org.trananh.shoppingappbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

@Service
public class JwtTokenProvider {
	
	private final String JWT_SECRET = "secretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkey";
	
	private final long JWT_EXPIRATION = 604800000L;
	
	@Autowired(required = true)
	private UserRepository userRepository;
	
	public String generateToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        
        // Tạo chuỗi json web token từ id của user.
        
        return Jwts.builder()
                   .setSubject(userId)
                   .setIssuedAt(now)
                   .setExpiration(expiryDate)
                   .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                   .compact();
    }
	
	public User getUserIdFromJWT(String token) {
        try {
        	Claims claims = Jwts.parser()
            		.setSigningKey(JWT_SECRET)
            		.build()
            		.parseClaimsJws(token)
            		.getBody();
        	
        	String id = String.valueOf(claims.getSubject());
        	
        	System.out.println(id);
        	
        	User user = userRepository.findById(id.trim()).orElse(null);
        	
        	System.out.println("User: " + user.toString());
        	
        	return user;
        	
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
    }

}

