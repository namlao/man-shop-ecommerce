package com.man.auth_service.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@GetMapping("/api/hello")
	public String hello(Authentication authentication) {
		return "Xin chao " + authentication.getName()+ ". Bạn đã truy cập thành công";
	}
}
