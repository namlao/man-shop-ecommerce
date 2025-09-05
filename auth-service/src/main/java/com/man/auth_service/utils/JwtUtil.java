package com.man.auth_service.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private final String SECRET_KEY ="my-secret-key-my-secret-key-my-secret-key";
	private final long ACCESS_TOKEN_EXPIRATION=1000*60*60;
	
	private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	
	public String generateToken(String username, String role) {
		HashMap<String, Object>  claims = new HashMap<>();
		claims.put("role", role);
		return Jwts.builder()
				.setSubject(username)
				.claim("role", role)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXPIRATION))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String extractUsername(String token) {
		return extractAllClaim(token).getSubject();
	}
	
	public String extractRole(String token) {
		return extractAllClaim(token).get("role",String.class);
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
			return true;
		}catch (JwtException e) {
			return false;
		}
	}
	
	public Claims extractAllClaim(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	
	public long getAccessTokenExpirySeconds() {
		return ACCESS_TOKEN_EXPIRATION/1000;
	}

}
