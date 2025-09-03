package com.man.ProductService.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DecreaseStockRequest {
	private Long id;
	private Long quanity;
}
