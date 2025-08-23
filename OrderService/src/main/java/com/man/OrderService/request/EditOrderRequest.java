package com.man.OrderService.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class EditOrderRequest {
	private Long id;

//	private Long userId;

	private List<Long> productId;

//	private Long totalPrice;

//	private Date orderDate;

	private String status;
}
