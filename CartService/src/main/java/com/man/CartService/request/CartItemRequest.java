package com.man.CartService.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {
		private Long productId;
		private Long quantity;
		private Long subTotal;

}
