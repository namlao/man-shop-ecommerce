package com.man.CartService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.man.CartService.config.FeignAuthInterceptor;
import com.man.CartService.response.ProductGetByIdResponse;

@FeignClient(name = "ProductService",configuration = FeignAuthInterceptor.class)
public interface ProductClient {
	
	@GetMapping("/{id}")
	ProductGetByIdResponse getById(@PathVariable("id") Long id);
}
