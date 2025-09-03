package com.man.OrderService.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DecreaseStockResponse {
	private Long id;

	private String name;

	private Long price;

	private Long quanity;
}
