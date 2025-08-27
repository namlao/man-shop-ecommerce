package com.man.CartService.request;

import java.util.List;

import com.man.CartService.entity.CartItem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteProductToCartRequest {
	private Long cartId;
	private List<CartItem> items;
	
}
