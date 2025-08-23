package com.man.OrderService.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductGetByIdResponse {
	private Long id;

	private String name;

	private Long price;
}
