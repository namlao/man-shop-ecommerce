package com.man.ProductService.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
//@NoArgsConstructor
public class ProductEditRequest {
	private Long id;
	private String name;
	private Long price;
	private int quanity;

}
