package com.man.ProductService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.man.ProductService.Entity.Product;
import com.man.ProductService.controller.ProductController;
import com.man.ProductService.request.DecreaseStockRequest;
import com.man.ProductService.request.ProductCreateRequest;
import com.man.ProductService.request.ProductDeleteRequest;
import com.man.ProductService.request.ProductEditRequest;
import com.man.ProductService.response.DecreaseStockResponse;
import com.man.ProductService.response.ListProductResponse;
import com.man.ProductService.response.ProductCreateReponse;
import com.man.ProductService.response.ProductDeleteResponse;
import com.man.ProductService.response.ProductEditReponse;
import com.man.ProductService.response.ProductGetByIdResponse;
import com.man.ProductService.service.ProductService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
	@Mock
	private ProductService productService;

	@InjectMocks
	private ProductController productController;
	
	@Test
	void listTest() {
		List<Product> products = List.of(new Product(100L, "Product 1", 50000L, 100L),
				new Product(100L, "Product 2", 50000L, 100L), new Product(100L, "Product 3", 50000L, 100L),
				new Product(100L, "Product 4", 50000L, 100L)

		);
		when(productService.getList()).thenReturn(products);
		
		ListProductResponse result = productController.list();
		assertNotNull(result);
		assertEquals(4, result.getProducts().size());
		
		verify(productService,times(1)).getList();
	}
	
	@Test
	void createTest() {
		ProductCreateRequest request = new ProductCreateRequest();
		request.setName("Product 1");
		request.setPrice(10000L);
		request.setQuanity(5);
		
		ProductCreateReponse response = new ProductCreateReponse(1L, request.getName(), request.getPrice(),  request.getQuanity());
		when(productService.create(any(ProductCreateRequest.class))).thenReturn(response);
		
		ProductCreateReponse result = productController.create(request);
		assertNotNull(result);
		assertEquals(request.getName(), result.getName());
		
		
		verify(productService,times(1)).create(request);
	}
	
	
	@Test
	void editTest() {
		ProductEditRequest request = new ProductEditRequest();
		request.setId(1L);
		request.setName("Product 1");
		request.setPrice(10000L);
		request.setQuanity(5);
		
		ProductEditReponse response = new ProductEditReponse(1L,"Product 2", 20000L, 10);
		when(productService.edit(any(ProductEditRequest.class))).thenReturn(response);
		
		ProductEditReponse result = productController.edit(request);
		
		assertNotNull(result);
		assertNotEquals(request.getName(), result.getName());
		
		
		verify(productService,times(1)).edit(request);
		 	
	}
	
	@Test
	void deleteTest() {
		Product product = new Product(100L, "Product 1", 50000L, 100L);
		
		when(productService.findById(anyLong())).thenReturn(product);
		
		ProductDeleteRequest request = new ProductDeleteRequest(100L);
		ResponseEntity<ProductDeleteResponse> responseEntity = productController.delete(request);
		
		assertNotNull(responseEntity);
		assertEquals(200L, responseEntity.getBody().getCode());
		verify(productService,times(1)).delete(anyLong());
		verify(productService,times(1)).findById(anyLong());
	}
	
	@Test
	void getByIdTest() {
		Long productId = 100L;
		
		ProductGetByIdResponse response = new ProductGetByIdResponse(100L, "Product 1", 50000L, 100);
		
		when(productService.getById(anyLong())).thenReturn(response);
		
		ProductGetByIdResponse result = productController.getById(productId);
		
		assertNotNull(result);
		assertEquals("Product 1", result.getName());
		
		verify(productService,times(1)).getById(anyLong());
	}
	
	@Test
	void decreaseStockTest() {
		DecreaseStockRequest request = new DecreaseStockRequest(100L,50L);
		DecreaseStockResponse response = new DecreaseStockResponse(100L,"Product 1",50000L,65L);
		when(productService.decreaseStock(request)).thenReturn(response);
		
		DecreaseStockResponse result = productController.decreaseStock(request);
		
		assertNotNull(result);
		assertEquals("Product 1", result.getName());
		verify(productService,times(1)).decreaseStock(request);
		
	}
}
