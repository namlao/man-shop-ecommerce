package com.man.ProductService.response;

import java.util.List;

import com.man.ProductService.Entity.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListProductResponse {
	private List<Product> products;

}
