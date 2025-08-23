package com.man.ProductService.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.man.ProductService.Entity.Product;
import com.man.ProductService.exception.ProductNotFoundException;
import com.man.ProductService.request.ProductCreateRequest;
import com.man.ProductService.request.ProductDeleteRequest;
import com.man.ProductService.request.ProductEditRequest;
import com.man.ProductService.response.ListProductResponse;
import com.man.ProductService.response.ProductCreateReponse;
import com.man.ProductService.response.ProductDeleteResponse;
import com.man.ProductService.response.ProductEditReponse;
import com.man.ProductService.response.ProductGetByIdResponse;
import com.man.ProductService.service.ProductService;

@RestController
public class ProductController {
	@Autowired
	private ProductService service;
	
	@Autowired
	private ModelMapper mapper;


	@GetMapping("/list")
	public ListProductResponse list() {
		List<Product> listProduct = service.getList();
		ListProductResponse resp = new ListProductResponse();
		resp.setProducts(listProduct);
		return resp;

	}

	@PostMapping("/create")
	public ProductCreateReponse create(@RequestBody ProductCreateRequest product) {
		return service.create(product);
	}

	@PutMapping("/edit")
	public ProductEditReponse edit(@RequestBody ProductEditRequest product) {
		return service.edit(product);

	}

	@DeleteMapping("/delete")
	public ResponseEntity<ProductDeleteResponse> delete(@RequestBody ProductDeleteRequest productReq) {
		try {
			Product product = service.getById(productReq.getId());
			service.delete(product.getId());

			return ResponseEntity.ok(new ProductDeleteResponse(200L, "Product deleted successfuly"));
		} catch (ProductNotFoundException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductDeleteResponse(404L, e.getMessage()));
		}

	}
	
	@GetMapping("/{id}")
	public ProductGetByIdResponse getById(@PathVariable Long id) {
		Product product = service.getById(id);
		
		return mapper.map(product, ProductGetByIdResponse.class);
		
		
	}
}
