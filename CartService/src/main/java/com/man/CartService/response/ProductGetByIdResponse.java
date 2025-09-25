package com.man.CartService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductGetByIdResponse {
	private Long id;
	
	private String name;
	
	private Long price;
	
	private Integer quanity;

}
