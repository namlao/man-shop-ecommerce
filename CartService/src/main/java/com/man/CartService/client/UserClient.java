package com.man.CartService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.man.CartService.response.UserGetByIdResponse;

@FeignClient(name="UserService")
public interface UserClient {
	@GetMapping("/i/{id}")
	UserGetByIdResponse getById(@PathVariable("id") Long id);
}
