package com.man.CartService.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetByIdResponse {
	private Long id;
	
	private Long userId;
	
	
	private List<CartItemResponse> items;
	
	private Long total;
}
