package com.man.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.man.auth_service.response.UserGetByIdResponse;
import com.man.auth_service.response.UserGetByUsernameResponse;



@FeignClient(name = "UserService")
public interface UserClient {
	@GetMapping("/u/{username}")
	UserGetByUsernameResponse getByUsername(@PathVariable("username") String username);
	
	
	@GetMapping("/i/{id}")
	UserGetByIdResponse getById(@PathVariable("id") Long id);
}
