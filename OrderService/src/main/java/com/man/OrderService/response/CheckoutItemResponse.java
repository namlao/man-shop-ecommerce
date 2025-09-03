package com.man.OrderService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutItemResponse {
	private Long productId;
    private String name;
    private Long requestedQuantity;
    private Long availableQuantity;
    private String status;
}
