package com.man.OrderService.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DecreaseStockRequest {
	private Long id;
	private Long quanity;
}
