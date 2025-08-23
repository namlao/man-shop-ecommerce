package com.man.OrderService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.man.OrderService.enity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

}
