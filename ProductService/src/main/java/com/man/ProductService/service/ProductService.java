package com.man.ProductService.service;

import java.util.List;

import com.man.ProductService.Entity.Product;
import com.man.ProductService.request.ProductCreateRequest;
import com.man.ProductService.request.ProductEditRequest;
import com.man.ProductService.response.ProductCreateReponse;
import com.man.ProductService.response.ProductEditReponse;

public interface ProductService{
	List<Product> getList();
	Product getById(Long id);
	List<Product> getByName(String name);
	ProductCreateReponse create(ProductCreateRequest product);
	ProductEditReponse edit(ProductEditRequest product);
	void delete(Long id);
	
	
}
