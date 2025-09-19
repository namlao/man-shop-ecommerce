package com.man.OrderService.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.man.OrderService.enity.Cart;
import com.man.OrderService.response.GetByIdResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


@FeignClient(name = "cart-server")
public interface CartClient {
	@GetMapping("/{id}")
	@CircuitBreaker(name = "cartService",fallbackMethod = "fallbackGetById")
	GetByIdResponse getById(@PathVariable("id") Long id);
	
	default GetByIdResponse fallbackGetById(Long id,Throwable throwable) {
		return new GetByIdResponse(id,0L,null,0L);
	}
	
	
	@DeleteMapping("/{cartId}")
	@CircuitBreaker(name = "cartService",fallbackMethod = "fallbackDeleteProductToCart")
	public void clearCart(@PathVariable("cartId") Long cartId);
	
	default void fallbackClearCart(Long cartId,Throwable throwable) {
		System.out.println("Cart id "+ cartId+" Not found");
	}
	
	
	@GetMapping("/user/{userId}")
	@CircuitBreaker(name = "cartService",fallbackMethod = "fallbackGetByUserId")
	public List<Cart> getByUserId(@PathVariable("userId") Long userId);
	
	default List<Cart> fallbackGetByUserId(Long userId,Throwable throwable) {
		List<Cart> listCart = new ArrayList<>();
		listCart.add(new Cart(-1L,userId,null,0L));
		return listCart;
	}
}
