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
public class OrderResponse {
	private Long id;
	private Long userId;
	private Long totalAmount;
	private String status;
	private List<OrderDetailResponse> items;
}
