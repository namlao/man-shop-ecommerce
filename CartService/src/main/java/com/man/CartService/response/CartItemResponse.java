package com.man.CartService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
	private Long id;
	private Long productId;
	private Long quantity;
	private Long subTotal;
}
