package com.man.OrderService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.man.OrderService.response.UserGetByIdResponse;
import com.man.OrderService.response.UserGetByUsernameResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient("UserService")
public interface UserClient {
	@GetMapping("/u/{username}")
	@CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetByUsername")
	UserGetByUsernameResponse getByUsername(@PathVariable("username") String username);

	default UserGetByUsernameResponse fallbackGetByUsername(String username, Throwable throwable) {
		System.out.println("UserService down! fallback ...");
		return new UserGetByUsernameResponse(-1L, username, "GUEST");
	}

	@GetMapping("/i/{id}")
	@CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetById")
	UserGetByIdResponse getById(@PathVariable("id") Long id);

	default UserGetByIdResponse fallbackGetById(Long id, Throwable throwable) {
		System.out.println("UserService down! fallback ...");
		return new UserGetByIdResponse(id, "Unknows", "GUEST");
	}
}
