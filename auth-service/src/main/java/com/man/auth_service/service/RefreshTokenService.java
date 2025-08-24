package com.man.auth_service.service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.man.auth_service.entity.RefreshToken;
import com.man.auth_service.repository.RefreshTokenReposittory;

@Service
public class RefreshTokenService{
	private RefreshTokenReposittory reposittory;
	
//	private UserClient client;
	
	private final Duration REFRESH_TTL = Duration.ofDays(7);

	public RefreshTokenService(RefreshTokenReposittory reposittory) {
		super();
		this.reposittory = reposittory;
	}
	
	
	public RefreshToken create(Long user) {
		RefreshToken rt = new RefreshToken();
		rt.setUserId(user);
		rt.setToken(UUID.randomUUID().toString());
		rt.setExpiryDate(Instant.now().plus(REFRESH_TTL));
		rt.setRevoked(false);
		return reposittory.save(rt);
	}
	
	@Transactional
	public RefreshToken rotate(String oldToken) {
		RefreshToken current = reposittory.findByToken(oldToken)
				.orElseThrow(()->new IllegalArgumentException("Invalid refresh token"));
		if(current.isRevoked() || current.getExpiryDate().isBefore(Instant.now())) {
			throw new IllegalStateException("Refresh token expired or revoked");
		}
		
		current.setRevoked(true);
		reposittory.save(current);
		
		return create(current.getUserId());
	}
	
	@Transactional
	public void revokeAllByUserId(Long userId) {
		reposittory.deleteByUserId(userId);
	}
	
	public Long getUserByToken(String token) {
		RefreshToken rt = reposittory.findByToken(token)
				.orElseThrow(()-> new IllegalArgumentException("Invalid refresh token"));
		if(rt.isRevoked() || rt.getExpiryDate().isBefore(Instant.now())) {
			throw new IllegalStateException("Refresh token expired or revoked");
		}
		
		return rt.getUserId();
	}
	
}
