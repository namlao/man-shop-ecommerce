package com.man.OrderService.enity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
	private Long id;
	
	private Long userId;
	
	
	private List<Product> items;
	
	private Long total;
}
