package com.man.auth_service.controller;

import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.man.auth_service.client.UserClient;
import com.man.auth_service.entity.RefreshToken;
import com.man.auth_service.response.UserGetByIdResponse;
import com.man.auth_service.response.UserGetByUsernameResponse;
import com.man.auth_service.service.RefreshTokenService;
import com.man.auth_service.utils.JwtUtil;

@RestController
//@RequestMapping("/auth")
public class AuthController {

	private final JwtUtil jwtUtil;

	private final UserClient client;

	private final RefreshTokenService refreshTokenService;

	private final PasswordEncoder encoder;

	public AuthController(JwtUtil jwtUtil, UserClient client, RefreshTokenService refreshTokenService,
			PasswordEncoder encoder) {
		super();
		this.jwtUtil = jwtUtil;
		this.client = client;
		this.refreshTokenService = refreshTokenService;
		this.encoder = encoder;
	}

	@PostMapping("/login")
	public Map<String, Object> login(@RequestBody Map<String, String> body) {

		String username = body.get("username");
		String rawPassword = body.get("password");

		UserGetByUsernameResponse user = client.getByUsername(username);

		if (user == null) {
			throw new RuntimeException("User not found");
		}

		if (!encoder.matches(rawPassword, user.getPassword())) {
			throw new RuntimeException("Invaild password");
		}

		String accessToken = jwtUtil.generateToken(user.getUsername());
		RefreshToken refreshToken = refreshTokenService.create(user.getId());

		return Map.of("token", "Bearer", "accessToken", accessToken, "expiresIn", jwtUtil.getAccessTokenExpirySeconds(),
				"refreshToken", refreshToken.getToken());
	}

	@PostMapping("/refresh")
	public Map<String, Object> refresh(@RequestBody Map<String, String> body) {
		String oldRefresh = body.get("refreshToken");

		Long userId = refreshTokenService.getUserByToken(oldRefresh);

		UserGetByIdResponse user = client.getById(userId);

		RefreshToken newRefresh = refreshTokenService.rotate(oldRefresh);

		String newAccess = jwtUtil.generateToken(user.getUsername());

		return Map.of("tokenType", "Bearer", "accessToken", newAccess, "expiresIn",
				jwtUtil.getAccessTokenExpirySeconds(), "refreshToken", newRefresh.getToken());
	}

	@PostMapping("/logout")
	public Map<String, String> logout(@RequestBody Map<String, String> body) {
		String username = body.get("username");

		UserGetByUsernameResponse user = client.getByUsername(username);

		refreshTokenService.revokeAllByUserId(user.getId());

		return Map.of("message", "Logged out");
	}

}
