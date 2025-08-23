package com.man.auth_service.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

	@GetMapping("/admin/hello")
	public String adminHello(Authentication authentication) {
		return "Xin chào Admin "+authentication.getName();
	}
	
	@GetMapping("/user/hello")
	public String adminUser(Authentication authentication) {
		return "Xin chào User "+authentication.getName();
	}
}
