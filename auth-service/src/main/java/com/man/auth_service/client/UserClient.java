package com.man.auth_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@FeignClient(name = "UserService")
public interface UserClient {
	@GetMapping("/{username}")
	public List<User> getByUsername(@PathVariable String username);
}
