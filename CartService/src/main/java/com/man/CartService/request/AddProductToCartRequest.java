package com.man.CartService.request;

import java.util.List;

import com.man.CartService.response.CartItemResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductToCartRequest {
	private Long id;
	private List<CartItemResponse> items;

}
