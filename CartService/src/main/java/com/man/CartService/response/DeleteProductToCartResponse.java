package com.man.CartService.response;

import java.util.List;

import com.man.CartService.entity.CartItem;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteProductToCartResponse {
	private Long id;
	private List<CartItemResponse> items;
	private Long total;
	
}
