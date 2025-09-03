package com.man.OrderService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
	private Long id;
	private Long productId;
	private String name;
	private Long quantity;
	private Long subTotal;
}