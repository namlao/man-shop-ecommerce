package com.man.CartService.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductToCartResponse {
	private Long id;
	private Long userId;
	private List<CartItemResponse> items;
	private Long total;
}
