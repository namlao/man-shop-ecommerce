package com.man.api_gateway.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtValidator {
	private final Key key;

	public JwtValidator(@Value("${security.jwt.secret:default-secret}") String secret) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public Claims parseClaims(String token) throws JwtException {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public boolean isExpired(Claims claims) {
		Date exp = claims.getExpiration();
		return exp != null && exp.before(new Date());
	}

	public Optional<String> getUsername(Claims claims) {
		return Optional.ofNullable(claims.getSubject());
	}

	public Optional<String> getRole(Claims claims) {
		return Optional.ofNullable(claims.get("role", String.class));
	}
}
