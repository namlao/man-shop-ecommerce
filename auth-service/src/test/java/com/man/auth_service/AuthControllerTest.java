package com.man.auth_service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.man.auth_service.controller.AuthController;
import com.man.auth_service.service.RefreshTokenService;

@SpringBootTest
@ActiveProfiles("test")
public class AuthControllerTest {
	
	@Mock
	private AuthController authController;
	
	@InjectMocks
	private RefreshTokenService refreshTokenService;

	public AuthControllerTest() {
		MockitoAnnotations.openMocks(this);
	}
	
//	@Test
//	void loginTest() {
//		Map<String, String> request = new HashMap<>();
//		request.put("username","testunit");
//		request.put("password","123456");
//	
//	}
}
