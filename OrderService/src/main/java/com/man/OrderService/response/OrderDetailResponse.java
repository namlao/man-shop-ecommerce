package com.man.OrderService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
	private Long productId;
    private String productName;
    private Long quantity;
    private Long price;
    private Long subTotal;
}
