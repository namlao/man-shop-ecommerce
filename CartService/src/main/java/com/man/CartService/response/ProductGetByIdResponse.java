package com.man.CartService.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductGetByIdResponse {
	private Long id;
	
	private String name;
	
	private Long price;
	
	private int quanity;

}
