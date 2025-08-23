package com.man.OrderService.response;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EditOrderResponse {
	private Long id;

	private Long userId;

	private List<Long> productId;

	private Long totalPrice;

	private Date orderDate;

	private String status;
}
