package com.man.auth_service.service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.man.auth_service.entity.RefreshToken;
import com.man.auth_service.entity.UserEntity;
import com.man.auth_service.repository.RefreshTokenReposittory;

@Service
public class RefreshTokenService{
	private RefreshTokenReposittory reposittory;
	
	private final Duration REFRESH_TTL = Duration.ofDays(7);

	public RefreshTokenService(RefreshTokenReposittory reposittory) {
		super();
		this.reposittory = reposittory;
	}
	
	
	public RefreshToken create(UserEntity user) {
		RefreshToken rt = new RefreshToken();
		rt.setUser(user);
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
		
		return create(current.getUser());
	}
	
	@Transactional
	public void revokeAllByUserId(Long userId) {
		reposittory.deleteByUser_Id(userId);
	}
	
	public UserEntity getUserByToken(String token) {
		RefreshToken rt = reposittory.findByToken(token)
				.orElseThrow(()-> new IllegalArgumentException("Invalid refresh token"));
		if(rt.isRevoked() || rt.getExpiryDate().isBefore(Instant.now())) {
			throw new IllegalStateException("Refresh token expired or revoked");
		}
		
		return rt.getUser();
	}
	
}
