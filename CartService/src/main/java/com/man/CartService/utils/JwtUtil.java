//package com.man.CartService.utils;
//
//import java.security.Key;
//
//import org.springframework.stereotype.Component;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//
//@Component
//public class JwtUtil {
//	private final String SECRET_KEY = "my-secret-key-my-secret-key-my-secret-key";
//
//	private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
//
//	public String extractUsername(String token) {
//		return extractAllClaim(token).getSubject();
//	}
//
//	public String extractRole(String token) {
//		return extractAllClaim(token).get("role", String.class);
//	}
//
//	public Claims extractAllClaim(String token) {
//		return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
//	}
//
//	public boolean validateToken(String token) {
//		try {
//			extractAllClaim(token);
//			return true;
//		} catch (JwtException e) {
//			// TODO: handle exception
//			return false;
//		}
//	}
//
//}
