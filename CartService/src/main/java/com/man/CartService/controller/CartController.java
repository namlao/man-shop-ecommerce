package com.man.CartService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.man.CartService.entity.Cart;
import com.man.CartService.request.AddProductToCartRequest;
import com.man.CartService.request.CartCreateRequest;
import com.man.CartService.request.DeleteProductToCartRequest;
import com.man.CartService.response.AddProductToCartResponse;
import com.man.CartService.response.CartCreateResponse;
import com.man.CartService.response.DeleteProductToCartResponse;
import com.man.CartService.service.CartService;

@RestController
public class CartController {

	@Autowired private CartService service;
	
	@PostMapping("/create")
	public CartCreateResponse create(@RequestBody CartCreateRequest cartReq) {
		
		return service.createCart(cartReq);
	}
	
	@PostMapping("/addProduct")
	public AddProductToCartResponse addProductToCart(@RequestBody AddProductToCartRequest cartReq) {
		return service.addProductToCart(cartReq);
	}
	
	@DeleteMapping("/deleteProduct")
	public DeleteProductToCartResponse deleteProductToCart(@RequestBody DeleteProductToCartRequest cartReq) {
		return service.removeProductFromCart(cartReq);
	}
	
	@DeleteMapping("/{cartId}")
	public void deleteProductToCart(@PathVariable("cartId") Long cartId) {
		service.clearCart(cartId);
	}
	
	@GetMapping("/user/{userId}")
	public List<Cart> getByUserId(@PathVariable("userId") Long userId){
		return service.getCartByUserId(userId);
	}
}
