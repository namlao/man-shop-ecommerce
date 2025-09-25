package com.man.CartService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.man.CartService.entity.CartItem;


@Repository
public interface ItemCartRepository extends JpaRepository<CartItem, Long>{
	List<CartItem> findByCartIdAndProductIdIn(Long cartId, List<Long> productIds);
	Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productIds);
	Optional<CartItem> findByCartId(Long cartId);
	void deleteByProductId(Long productId);
}
