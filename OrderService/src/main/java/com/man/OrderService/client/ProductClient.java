package com.man.OrderService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.man.OrderService.response.ProductGetByIdResponse;

@FeignClient(name = "ProductService")
public interface ProductClient {
	@GetMapping("/{id}")
	ProductGetByIdResponse getById(@PathVariable("id") Long id);

}
