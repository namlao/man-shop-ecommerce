package com.man.OrderService.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetByIdResponse {
	private Long id;
	
	private Long userId;
	
	
	private List<CartItemResponse> items;
	
	private Long total;
	
	
}
