package com.man.CartService.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponse {
	private Long id;
	private Long productId;
	private Long quantity;
	private Long subTotal;
}
