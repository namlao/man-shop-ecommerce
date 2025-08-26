package com.man.CartService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.man.CartService.entity.CartItem;
import java.util.List;


@Repository
public interface ItemCartRepository extends JpaRepository<CartItem, Long>{
	List<CartItem> findByProductId(Long productId);
	void deleteByProductId(Long productId);
}
