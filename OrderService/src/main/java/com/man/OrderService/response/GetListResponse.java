package com.man.OrderService.response;

import java.util.List;

import com.man.OrderService.enity.Order;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetListResponse {
	private List<Order> orders;

}
