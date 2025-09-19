package com.man.ProductService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DecreaseStockResponse {
	private Long id;

	private String name;

	private Long price;

	private Long quanity;
}
