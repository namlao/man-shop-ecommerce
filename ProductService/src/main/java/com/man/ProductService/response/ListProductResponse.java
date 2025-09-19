package com.man.ProductService.response;

import java.util.List;

import com.man.ProductService.Entity.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListProductResponse {
	private List<Product> products;

}
