package com.man.CartService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.man.CartService.config.FeignAuthInterceptor;
import com.man.CartService.response.ProductGetByIdResponse;
import com.man.CartService.response.UserGetByIdResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient(name = "product-service",configuration = FeignAuthInterceptor.class)
public interface ProductClient {
	
	@GetMapping("/{id}")
	@CircuitBreaker(name = "productService", fallbackMethod = "fallbackGetById")
	ProductGetByIdResponse getById(@PathVariable("id") Long id);
	
	default ProductGetByIdResponse fallbackGetById( Long id,Throwable throwable) {
		System.out.println("ProductService down! fallback ...");
		return new ProductGetByIdResponse(-1L,"Unknown Product",0L,0);
	}
}
