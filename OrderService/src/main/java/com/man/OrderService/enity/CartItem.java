package com.man.OrderService.enity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
	private Long id;

	private Cart cart;

	private Long productId;
	
	private Long quantity;
	
	private Long subTotal;
}
