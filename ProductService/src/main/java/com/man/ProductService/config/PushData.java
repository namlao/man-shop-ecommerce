package com.man.ProductService.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.man.ProductService.Entity.Product;
import com.man.ProductService.Repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PushData implements CommandLineRunner{
	private final ProductRepository repository;
	

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		List<Product> listProduct = new ArrayList<>();
		listProduct.add(new Product(null, "Product 1",10000L,100L));
		listProduct.add(new Product(null, "Product 2",20000L,100L));
		listProduct.add(new Product(null, "Product 3",30000L,100L));
		listProduct.add(new Product(null, "Product 4",40000L,100L));
		listProduct.add(new Product(null, "Product 5",50000L,100L));
		listProduct.add(new Product(null, "Product 6",50000L,10L));
		listProduct.add(new Product(null, "Product 7",8000L,2L));
		listProduct.add(new Product(null, "Product 8",60000L,7L));
		listProduct.stream().forEach(item->repository.save(item));
	}

}
