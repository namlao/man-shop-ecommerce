package com.man.auth_service.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "refresh_tokens")
@Getter @Setter
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false,unique = true, length = 200)
	private String token;
	
	@Column(nullable = false)
	private Long userId;
	
	@Column(nullable = false)
	private Instant expiryDate;
	
	@Column(nullable = false)
	private boolean revoked = false;
}
