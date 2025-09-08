package com.man.OrderService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.man.OrderService.request.DecreaseStockRequest;
import com.man.OrderService.response.DecreaseStockResponse;
import com.man.OrderService.response.ProductGetByIdResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient(name = "product-service")
public interface ProductClient {
	@GetMapping("/{id}")
	@CircuitBreaker(name = "productService", fallbackMethod = "fallbackGetById")
	ProductGetByIdResponse getById(@PathVariable("id") Long id);
	default ProductGetByIdResponse fallbackGetById(Long id,Throwable throwable) {
		return new ProductGetByIdResponse(id,"Unknown Product",0L,0L);
	}
	

	@PutMapping("/decreaseStock")
	@CircuitBreaker(name = "productService", fallbackMethod = "fallbackDecreaseStock")
	DecreaseStockResponse decreaseStock(@RequestBody DecreaseStockRequest req);
	default DecreaseStockResponse fallbackGetById(DecreaseStockRequest req,Throwable throwable) {
		return new DecreaseStockResponse(req.getId(),"Unknow Product",0L,req.getQuanity());
	}
}
