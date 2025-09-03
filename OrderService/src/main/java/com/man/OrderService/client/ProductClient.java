package com.man.OrderService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.man.OrderService.request.DecreaseStockRequest;
import com.man.OrderService.response.DecreaseStockResponse;
import com.man.OrderService.response.ProductGetByIdResponse;

@FeignClient(name = "ProductService")
public interface ProductClient {
	@GetMapping("/{id}")
	ProductGetByIdResponse getById(@PathVariable("id") Long id);

	@PutMapping("/decreaseStock")
	DecreaseStockResponse decreaseStock(@RequestBody DecreaseStockRequest req);
}
