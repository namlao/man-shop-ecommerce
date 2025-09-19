package com.man.CartService.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cartItem")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "cart_id")
	@JsonBackReference
	private Cart cart;

	@Column(nullable = false)
	private Long productId;
	
	@Column(nullable = false)
	private Long quantity;
	
	private Long subTotal;
}
