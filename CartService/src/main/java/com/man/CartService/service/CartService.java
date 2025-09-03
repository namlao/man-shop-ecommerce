package com.man.CartService.service;

import java.util.List;

import com.man.CartService.entity.Cart;
import com.man.CartService.request.AddProductToCartRequest;
import com.man.CartService.request.CartCreateRequest;
import com.man.CartService.request.DeleteProductToCartRequest;
import com.man.CartService.response.AddProductToCartResponse;
import com.man.CartService.response.CartCreateResponse;
import com.man.CartService.response.DeleteProductToCartResponse;
import com.man.CartService.response.GetByIdResponse;

public interface CartService {
	CartCreateResponse createCart(CartCreateRequest cart);

	AddProductToCartResponse addProductToCart(AddProductToCartRequest item);

	DeleteProductToCartResponse removeProductFromCart(DeleteProductToCartRequest cartReq);

	void clearCart(Long Id);

	List<Cart> getCartByUserId(Long userId);

	GetByIdResponse getById(Long id);
}
