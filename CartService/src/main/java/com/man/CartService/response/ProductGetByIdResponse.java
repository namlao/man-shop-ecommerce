package com.man.CartService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductGetByIdResponse {
	private Long id;
	
	private String name;
	
	private Long price;
	
	private int quanity;

}
