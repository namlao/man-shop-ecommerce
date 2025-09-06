package com.man.OrderService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ProductGetByIdResponse {
	private Long id;
	private String name;
	private Long quanity;
	private Long price;

}

