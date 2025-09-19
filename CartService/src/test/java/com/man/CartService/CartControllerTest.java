package com.man.CartService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import com.man.CartService.controller.CartController;
import com.man.CartService.entity.Cart;
import com.man.CartService.entity.CartItem;
import com.man.CartService.request.AddProductToCartRequest;
import com.man.CartService.request.CartCreateRequest;
import com.man.CartService.request.CartItemRequest;
import com.man.CartService.request.DeleteProductToCartRequest;
import com.man.CartService.response.AddProductToCartResponse;
import com.man.CartService.response.CartCreateResponse;
import com.man.CartService.response.CartItemResponse;
import com.man.CartService.response.DeleteProductToCartResponse;
import com.man.CartService.response.GetByIdResponse;
import com.man.CartService.response.UserGetByUsernameResponse;
import com.man.CartService.service.CartService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CartControllerTest {
	@Mock
	private CartService cartService;

	@InjectMocks
	CartController cartController;

	@Test
	void createTest() {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("testuser", null,
				List.of());
		SecurityContextHolder.getContext().setAuthentication(token);

		UserGetByUsernameResponse user = new UserGetByUsernameResponse(101L, token.getName(), "User");

		List<CartItemRequest> itemRequests = List.of(new CartItemRequest(100L, 10L, 20000L),
				new CartItemRequest(101L, 20L, 30000L));

		CartCreateRequest request = new CartCreateRequest();
		request.setUserId(user.getId());
		request.setItems(itemRequests);

		List<CartItemResponse> itemResponses = List.of(new CartItemResponse(1L, 100L, 10L, 20000L),
				new CartItemResponse(1L, 101L, 20L, 30000L));

		CartCreateResponse response = new CartCreateResponse();
		response.setId(1L);
		response.setUserId(user.getId());
		response.setItems(itemResponses);
		response.setTotal(50000L);

		when(cartService.createCart(request)).thenReturn(response);

		CartCreateResponse result = cartController.create(request);

		assertNotNull(result);
//		assertEquals(50000L, result.getTotal());
//		assertEquals(2, result.getItems().size());
//		assertEquals(user.getId(), result.getUserId());

		verify(cartService, times(1)).createCart(any(CartCreateRequest.class));

	}

	@Test
	void addProductToCartTest() {
		// Giả lập user đăng nhập
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("testuser", null,
				List.of());
		SecurityContextHolder.getContext().setAuthentication(token);

		UserGetByUsernameResponse user = new UserGetByUsernameResponse(101L, token.getName(), "User");

		// giả lập request
		AddProductToCartRequest request = new AddProductToCartRequest(1L,
				List.of(new CartItemRequest(102L, 20L, 20000L), new CartItemRequest(103L, 20L, 30000L)));

		AddProductToCartResponse response = new AddProductToCartResponse(1L, user.getId(),
				List.of(new CartItemResponse(1L, 100L, 10L, 20000L), new CartItemResponse(1L, 101L, 20L, 30000L),
						new CartItemResponse(1L, 102L, 20L, 20000L), new CartItemResponse(1L, 103L, 20L, 30000L)),
				1000000L);

		when(cartService.addProductToCart(any(AddProductToCartRequest.class))).thenReturn(response);

		AddProductToCartResponse result = cartController.addProductToCart(request);

		assertNotNull(result);
//		assertEquals(1000000L, result.getTotal());
//		assertEquals(4, result.getItems().size());
//		assertEquals(user.getId(), result.getUserId());

		verify(cartService, times(1)).addProductToCart(any(AddProductToCartRequest.class));
	}

	@Test
	void deleteProductToCartTest() {
		DeleteProductToCartRequest request = new DeleteProductToCartRequest(1L,
				List.of(new CartItem(1L, null, 100L, 10L, 20000L), new CartItem(1L, null, 101L, 20L, 30000L)));

		DeleteProductToCartResponse response = new DeleteProductToCartResponse(1L,
				List.of(new CartItemResponse(1L, 100L, 10L, 20000L)), 50000L);
		when(cartService.removeProductFromCart(request)).thenReturn(response);
		
		DeleteProductToCartResponse result = cartController.deleteProductToCart(request);
		
		assertNotNull(result);
		verify(cartService,times(1)).removeProductFromCart(request);
		
	}
	
	@Test
	void clearCartTest() {
		Long cartId = 1L;
		
		cartController.clearCart(cartId);
		
		verify(cartService,times(1)).clearCart(cartId);
		
	}
	
	@Test
	void getByUserIdTest() {
		Long userId = 100L;
		List<Cart> list = cartController.getByUserId(userId);
		
		assertNotNull(list);
		
		verify(cartService,times(1)).getCartByUserId(userId);
		
	}
	
	@Test
	void getByIdTest() {
		Long id = 100L;
		
		GetByIdResponse response = new GetByIdResponse(id,100L,List.of(),100000L);
		
		when(cartService.getById(id)).thenReturn(response);
		
		GetByIdResponse rs = cartController.getById(id);
		
		
		assertNotNull(rs);
		
		verify(cartService,times(1)).getById(id);
		
	}
}
