package com.man.OrderService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.man.OrderService.response.GetByIdResponse;


@FeignClient(name = "CartService")
public interface CartClient {
	@GetMapping("/{id}")
	GetByIdResponse getById(@PathVariable("id") Long id);
	
	@DeleteMapping("/{cartId}")
	public void deleteProductToCart(@PathVariable("cartId") Long cartId);
}
