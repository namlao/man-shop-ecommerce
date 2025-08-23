package com.man.ProductService.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductEditReponse {

	private Long id;
	private String name;
	private Long price;
	private int quanity;

}
