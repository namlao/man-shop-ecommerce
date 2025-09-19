package com.man.CartService.request;

import java.util.List;

import com.man.CartService.response.CartItemResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProductToCartRequest {
	private Long id;
	private List<CartItemRequest> items;

}
