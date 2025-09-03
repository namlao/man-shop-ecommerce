package com.man.OrderService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.man.OrderService.enity.Order;
import com.man.OrderService.exception.OrderNotFoundException;
import com.man.OrderService.request.CreateOrderRequest;
import com.man.OrderService.request.EditOrderRequest;
import com.man.OrderService.request.OrderDeleteRequest;
import com.man.OrderService.response.CheckoutResponse;
import com.man.OrderService.response.CreateOrderResponse;
import com.man.OrderService.response.EditOrderResponse;
import com.man.OrderService.response.GetListResponse;
import com.man.OrderService.response.OrderDeleteResponse;
import com.man.OrderService.service.OrderService;

@RestController
public class OrderController {
	
	@Autowired private OrderService service;
	
	@GetMapping("/list")
	public GetListResponse list(){
		GetListResponse resp = new GetListResponse();
		resp.setOrders(service.listOrder());
		return resp;
	}
	
	@PostMapping("/create")
	public CreateOrderResponse create(@RequestBody CreateOrderRequest orderReq) {
		return service.create(orderReq);
	}
	
	@PutMapping("/edit")
	public EditOrderResponse edit(@RequestBody EditOrderRequest order) {
		return service.edit(order);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<OrderDeleteResponse>  delete(@RequestBody OrderDeleteRequest orderReq) {
		try {
			Order order = service.getById(orderReq.getId());
			service.delete(order.getId());

			return ResponseEntity.ok(new OrderDeleteResponse(200L, "Order deleted successfuly"));
		} catch (OrderNotFoundException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderDeleteResponse(404L, e.getMessage()));
		}
	}
	
	@GetMapping("/checkout/{cartId}")
	public CheckoutResponse checkout(@PathVariable("cartId") Long cartId) {
		return service.checkout(cartId);
	}
	

}
