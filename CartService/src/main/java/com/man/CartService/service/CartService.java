package com.man.CartService.service;

import java.util.List;

import com.man.CartService.entity.Cart;
import com.man.CartService.entity.CartItem;
import com.man.CartService.request.AddProductToCartRequest;
import com.man.CartService.request.CartCreateRequest;
import com.man.CartService.response.AddProductToCartResponse;
import com.man.CartService.response.CartCreateResponse;

public interface CartService {
	CartCreateResponse createCart(CartCreateRequest cart);

	AddProductToCartResponse addProductToCart(AddProductToCartRequest item);

	Cart removeProductFromCart(Long carId, List<CartItem> items);

	void clearCart(Long Id);

	List<Cart> getCartByUserId(Long userId);
}
