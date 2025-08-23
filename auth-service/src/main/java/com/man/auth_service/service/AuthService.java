package com.man.auth_service.service;

import org.springframework.stereotype.Service;

import com.man.auth_service.dto.LoginRequest;
import com.man.auth_service.repository.UserRepository;

@Service
public class AuthService {
	private UserRepository userRepository;

	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public boolean login(LoginRequest request) {
		return userRepository.findByUsername(request.getUsername())
				.map(user -> user.getPassword().equals(request.getPassword())).orElse(false);
	}
}
