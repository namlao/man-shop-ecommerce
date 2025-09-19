package com.man.CartService.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartItemRequest {
		private Long productId;
		private Long quantity;
		private Long subTotal;

}
