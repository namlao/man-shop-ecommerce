package com.man.ProductService.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.man.ProductService.Entity.Product;
import com.man.ProductService.Repository.ProductRepository;
import com.man.ProductService.exception.ProductNotFoundException;
import com.man.ProductService.request.DecreaseStockRequest;
import com.man.ProductService.request.ProductCreateRequest;
import com.man.ProductService.request.ProductEditRequest;
import com.man.ProductService.response.DecreaseStockResponse;
import com.man.ProductService.response.ProductCreateReponse;
import com.man.ProductService.response.ProductEditReponse;
import com.man.ProductService.response.ProductGetByIdResponse;
import com.man.ProductService.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository repository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public List<Product> getList() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public ProductCreateReponse create(ProductCreateRequest product) {

		Product entity = repository.save(mapper.map(product, Product.class));

		return mapper.map(entity, ProductCreateReponse.class);
	}

	@Override
	public ProductEditReponse edit(ProductEditRequest productEdit) {

		Product product = repository.findById(productEdit.getId())
				.orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productEdit.getId()));

		product = mapper.map(productEdit, Product.class);

		return mapper.map(repository.save(product), ProductEditReponse.class);
	}

	@Override
	public void delete(Long id) {
		Product product = repository.findById(id)
				.orElseThrow(()->new ProductNotFoundException("Product not found with id "+ id));
		
		repository.delete(product);

	}

	@Override
	public ProductGetByIdResponse getById(Long id) {
		// TODO Auto-generated method stub
		Product product = repository.findById(id)
				.orElseThrow(()->new ProductNotFoundException("Product not found with id "+ id));
		return mapper.map(product, ProductGetByIdResponse.class);
	}

	@Override
	public List<Product> getByName(String name) {
		// TODO Auto-generated method stub
		return repository.findByName(name);
	}
	
	@Override
	public Product findById(Long id) {
		// TODO Auto-generated method stub
		Product product = repository.findById(id)
				.orElseThrow(()->new ProductNotFoundException("Product not found with id "+ id));
		return product;
	}

	@Override
	public DecreaseStockResponse decreaseStock(DecreaseStockRequest req) {
		// TODO Auto-generated method stub
		Product product = repository.findById(req.getId())
				.orElseThrow(()->new ProductNotFoundException("Product not found with id "+ req.getId()));
		
		product.setQuanity(product.getPrice()-req.getQuanity());
		
		return mapper.map(repository.save(product), DecreaseStockResponse.class);
	}

}
