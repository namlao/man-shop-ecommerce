package com.man.OrderService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.man.OrderService.enity.OrderDetail;

public interface OrderItemRepository extends JpaRepository<OrderDetail, Long>{

}
