//package com.man.api_gateway.utils;
//
//import org.springframework.stereotype.Component;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//
//@Component
//public class JwtUtil {
//
//    private final String SECRET_KEY = "my-secret-key-my-secret-key-my-secret-key"; // phải trùng với Auth-Service
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token);
//            return true;
//        } catch (JwtException e) {
//            return false;
//        }
//    }
//
//    
//	public String extractUsername(String token) {
//		return extractAllClaim(token).getSubject();
//	}
//	
//	public String extractRole(String token) {
//		return extractAllClaim(token).get("role",String.class);
//	}
//    
//    public Claims extractAllClaim(String token) {
//		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
//	}
//}
