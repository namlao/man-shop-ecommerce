package com.man.ProductService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import com.man.ProductService.Entity.Product;
import com.man.ProductService.Repository.ProductRepository;
import com.man.ProductService.request.DecreaseStockRequest;
import com.man.ProductService.request.ProductCreateRequest;
import com.man.ProductService.request.ProductEditRequest;
import com.man.ProductService.response.DecreaseStockResponse;
import com.man.ProductService.response.ProductCreateReponse;
import com.man.ProductService.response.ProductEditReponse;
import com.man.ProductService.response.ProductGetByIdResponse;
import com.man.ProductService.service.impl.ProductServiceImpl;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ProductServiceTests {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductServiceImpl impl;

	private ModelMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new ModelMapper();
		impl = new ProductServiceImpl(productRepository, mapper);
	}

	@Test
	void getListTest() {
		List<Product> products = List.of(new Product(100L, "Product 1", 50000L, 100L),
				new Product(100L, "Product 2", 50000L, 100L), new Product(100L, "Product 3", 50000L, 100L),
				new Product(100L, "Product 4", 50000L, 100L)

		);

		when(productRepository.findAll()).thenReturn(products);

		List<Product> result = impl.getList();
		assertNotNull(result);
		assertEquals(products.size(), result.size());

		verify(productRepository, times(1)).findAll();
	}

	@Test
	void createTest() {
		ProductCreateRequest request = new ProductCreateRequest("Product 1", 50000L, 100);

//		ProductCreateReponse response = new ProductCreateReponse(1L, "Product 1", 50000L, 100);

		when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

		ProductCreateReponse result = impl.create(request);
		assertNotNull(result);
		assertEquals(request.getName(), result.getName());

		verify(productRepository, times(1)).save(any(Product.class));

	}

	@Test
	void editTest() {
		Product product = new Product(100L, "Product 1", 50000L, 100L);
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

		ProductEditRequest request = new ProductEditRequest(100L, "Product 2", 60000L, 1000);

		when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

		ProductEditReponse result = impl.edit(request);

		assertNotNull(result);
		assertEquals(request.getName(), result.getName());
		assertNotEquals(product.getName(), result.getName());

		verify(productRepository, times(1)).findById(anyLong());
		verify(productRepository, times(1)).save(any(Product.class));
	}

	@Test
	void deleteTest() {
		Product product = new Product(100L, "Product 1", 50000L, 100L);
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

		impl.delete(product.getId());

		verify(productRepository, times(1)).findById(anyLong());
		verify(productRepository, times(1)).delete(any(Product.class));

	}

	@Test
	void getByIdTest() {
		Product product = new Product(100L, "Product 1", 50000L, 100L);
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

		ProductGetByIdResponse result = impl.getById(product.getId());
		assertNotNull(result);

		verify(productRepository, times(1)).findById(anyLong());

	}

	@Test
	void getByNameTest() {
		Product product = new Product(100L, "Product 1", 50000L, 100L);
		Product productTest = new Product(101L, "Product 2", 50000L, 100L);

		when(productRepository.findByName(anyString())).thenReturn(List.of(product));
		List<Product> result = impl.getByName(product.getName());
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(product.getName(), result.get(0).getName());
		assertNotEquals(productTest.getName(), result.get(0).getName());

		verify(productRepository, times(1)).findByName(anyString());

	}

	@Test
	void findByIdTest() {
		Product product = new Product(100L, "Product 1", 50000L, 100L);
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

		Product result = impl.findById(102L);
		assertNotNull(result);
		assertEquals("Product 1", result.getName());

		verify(productRepository, times(1)).findById(anyLong());
	}

	@Test
	void decreaseStockTest() {
		Product product = new Product(100L, "Product 1", 50000L, 100L);
		
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
		when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
		
		DecreaseStockRequest request = new DecreaseStockRequest(100L, 50L);

		DecreaseStockResponse result = impl.decreaseStock(request);
		assertNotNull(result);
		assertEquals(50, result.getQuanity());

		verify(productRepository, times(1)).findById(anyLong());

	}
}
