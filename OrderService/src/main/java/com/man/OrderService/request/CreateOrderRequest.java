package com.man.OrderService.request;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

	private Long userId;
	
	private List<Long> productId;

//	private Long totalPrice;

	private Date orderDate;

	private String status;
}
