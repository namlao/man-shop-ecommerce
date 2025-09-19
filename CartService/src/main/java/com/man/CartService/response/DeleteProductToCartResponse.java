package com.man.CartService.response;

import java.util.List;

import com.man.CartService.entity.CartItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteProductToCartResponse {
	private Long id;
	private List<CartItemResponse> items;
	private Long total;
	
}
