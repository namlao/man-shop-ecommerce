package com.man.OrderService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DecreaseStockResponse {
	private Long id;

	private String name;

	private Long price;

	private Long quanity;
}
