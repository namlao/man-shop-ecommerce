package com.man.OrderService.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.man.OrderService.client.ProductClient;
import com.man.OrderService.enity.Order;
import com.man.OrderService.repository.OrderRepository;
import com.man.OrderService.request.CreateOrderRequest;
import com.man.OrderService.request.EditOrderRequest;
import com.man.OrderService.response.CreateOrderResponse;
import com.man.OrderService.response.EditOrderResponse;
import com.man.OrderService.response.ProductGetByIdResponse;
import com.man.OrderService.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository repository;

	@Autowired
	private ModelMapper mapper;
	
	@Autowired private ProductClient client;

	@Override
	public List<Order> listOrder() {
		// TODO Auto-generated method stub
		
		return repository.findAll() ;
	}

	@Override
	public CreateOrderResponse create(CreateOrderRequest orderReq) {
		// TODO Auto-generated method stub
		Long total = 0L;
		Order entity = mapper.map(orderReq, Order.class);
		
		for (Long productId : orderReq.getProductId()) {
			ProductGetByIdResponse product = client.getById(productId);
			total += product.getPrice();
		}
		entity.setTotalPrice(total);
		entity = repository.save(entity);
		return mapper.map(entity, CreateOrderResponse.class);
//		return null;
	}

	@Override
	public EditOrderResponse edit(EditOrderRequest orderEdit) {
//		Long total = 0L;
		// TODO Auto-generated method stub
		Order order = repository.findById(orderEdit.getId())
				.orElseThrow(() -> new RuntimeException("Order not found"));
		if(orderEdit.getStatus() != null) {
			order.setStatus(orderEdit.getStatus());
			
		}
		
		if(orderEdit.getProductId() != null && !orderEdit.getProductId().equals(order.getProductId())) {
			Long total = 0L;
			order.setProductId(orderEdit.getProductId());
			for (Long productId : orderEdit.getProductId()) {
				ProductGetByIdResponse product = client.getById(productId);
				total += product.getPrice();
			}
			order.setTotalPrice(total);
		}
		
		
//		order = mapper.map(orderEdit, Order.class);
		System.out.println(order);
		return mapper.map(repository.save(order), EditOrderResponse.class);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		Order order = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Order not found"));
		
		repository.delete(order);

	}

	@Override
	public Order getById(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id).get();
	}

}
