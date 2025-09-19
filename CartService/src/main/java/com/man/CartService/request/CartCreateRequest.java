package com.man.CartService.request;

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
public class CartCreateRequest {
	private Long userId;
	private List<CartItemRequest> items;
}
