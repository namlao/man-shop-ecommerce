package com.man.OrderService.service;

import java.util.List;

import com.man.OrderService.enity.Order;
import com.man.OrderService.response.CheckoutResponse;
import com.man.OrderService.response.OrderResponse;

public interface OrderService {
//	CreateOrderResponse create(CreateOrderRequest order);
//	EditOrderResponse edit(EditOrderRequest order);
//	void delete(Long id);
	
//	List<OrderResponse> listOrder();
	List<OrderResponse> getByUserId(String username);
//	Long getUserId(Authentication authentication);
	Order getById(Long id);
	CheckoutResponse checkout(Long cartId);

}
