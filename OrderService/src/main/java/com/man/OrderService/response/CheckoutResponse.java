package com.man.OrderService.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutResponse {
	private Long cartId;
    private List<CheckoutItemResponse> items;
    private Long totalPaid;
}
