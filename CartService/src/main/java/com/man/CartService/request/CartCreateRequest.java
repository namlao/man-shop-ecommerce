package com.man.CartService.request;

import java.util.List;

import com.man.CartService.entity.CartItem;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartCreateRequest {
	private Long id;
	private Long userId;
	private List<CartItem> items;
}
