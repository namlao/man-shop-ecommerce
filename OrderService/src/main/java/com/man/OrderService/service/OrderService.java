package com.man.OrderService.service;

import java.util.List;

import com.man.OrderService.enity.Order;
import com.man.OrderService.request.CreateOrderRequest;
import com.man.OrderService.request.EditOrderRequest;
import com.man.OrderService.response.CheckoutResponse;
import com.man.OrderService.response.CreateOrderResponse;
import com.man.OrderService.response.EditOrderResponse;

public interface OrderService {
	List<Order> listOrder();
	CreateOrderResponse create(CreateOrderRequest order);
	EditOrderResponse edit(EditOrderRequest order);
	void delete(Long id);
	Order getById(Long id);
	CheckoutResponse checkout(Long cartId);

}
