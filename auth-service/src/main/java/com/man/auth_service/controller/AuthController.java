package com.man.auth_service.controller;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.man.auth_service.entity.RefreshToken;
import com.man.auth_service.entity.UserEntity;
import com.man.auth_service.repository.UserRepository;
import com.man.auth_service.service.RefreshTokenService;
import com.man.auth_service.utils.JwtUtil;

@RestController
//@RequestMapping("/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;

	private final JwtUtil jwtUtil;

	private final UserRepository userRepository;

	private final RefreshTokenService refreshTokenService;

	public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository,
			RefreshTokenService refreshTokenService) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.userRepository = userRepository;
		this.refreshTokenService = refreshTokenService;
	}

	@PostMapping("/login")
	public Map<String, Object> login(@RequestBody Map<String,String> body) {
	    
		 Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        body.get("username"),body.get("password"))
	        );
	        String username = authentication.getName();
	        String accessToken = jwtUtil.generateToken(username);
	        
	        UserEntity user = userRepository.findByUsername(username)
	        		.orElseThrow(()-> new RuntimeException("User not found"));
	        
	        RefreshToken refreshToken = refreshTokenService.create(user);
	        return Map.of(
	        		"token", "Bearer",
	        		"accessToken", accessToken,
	        		"expiresIn",jwtUtil.getAccessTokenExpirySeconds(),
	        		"refreshToken",refreshToken.getToken()
	        		);
	}
	
	@PostMapping("/refresh")
	public Map<String, Object> refresh(@RequestBody Map<String,String> body){
		String oldRefresh = body.get("refreshToken");
		
		UserEntity user = refreshTokenService.getUserByToken(oldRefresh);
		
		RefreshToken newRefresh = refreshTokenService.rotate(oldRefresh);
		
		String newAccess = jwtUtil.generateToken(user.getUsername());
		
		return Map.of(
					"tokenType","Bearer",
					"accessToken",newAccess,
					"expiresIn",jwtUtil.getAccessTokenExpirySeconds(),
					"refreshToken",newRefresh.getToken()
				);
	}
	
	@PostMapping("/logout")
	public Map<String, String> logout(@RequestBody Map<String,String> body){
		String username = body.get("username");
		
		UserEntity user = userRepository.findByUsername(username)
				.orElseThrow(()-> new RuntimeException("User not found"));
		refreshTokenService.revokeAllByUserId(user.getId());
		
		return Map.of("message","Logged out");
	}

}
