package com.man.CartService.service;

import java.util.List;

import com.man.CartService.entity.Cart;
import com.man.CartService.entity.CartItem;

public interface CartService {
	Cart createCart(Cart cart);

	Cart addProductToCart(Long cartiId,List<CartItem> item);

	Cart removeProductFromCart(Long carId, List<CartItem> items);

	void clearCart(Long Id);

	List<Cart> getCartByUserId(Long userId);
}
