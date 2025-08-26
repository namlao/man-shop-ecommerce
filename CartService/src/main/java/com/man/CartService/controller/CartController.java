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
import com.man.CartService.entity.CartItem;
import com.man.CartService.service.CartService;

@RestController
public class CartController {

	@Autowired private CartService service;
	
	@PostMapping("/create")
	public Cart create(@RequestBody Cart cartReq) {
		return service.createCart(cartReq);
	}
	
	@PostMapping("/{cartId}/product")
	public Cart addProductToCart(@PathVariable("cartId") Long cartId, @RequestBody List<CartItem> cartReq) {
		return service.addProductToCart(cartId,cartReq);
	}
	
	@DeleteMapping("/{cartId}/product")
	public Cart deleteProductToCart(@PathVariable("cartId") Long cartId, @RequestBody List<CartItem> cartReq) {
		return service.removeProductFromCart(cartId,cartReq);
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
