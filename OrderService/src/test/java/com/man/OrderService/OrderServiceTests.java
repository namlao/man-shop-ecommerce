package com.man.OrderService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import com.man.OrderService.client.CartClient;
import com.man.OrderService.client.ProductClient;
import com.man.OrderService.client.UserClient;
import com.man.OrderService.enity.Cart;
import com.man.OrderService.enity.CartItem;
import com.man.OrderService.enity.Order;
import com.man.OrderService.enity.OrderDetail;
import com.man.OrderService.repository.OrderRepository;
import com.man.OrderService.response.CartItemResponse;
import com.man.OrderService.response.CheckoutResponse;
import com.man.OrderService.response.GetByIdResponse;
import com.man.OrderService.response.OrderResponse;
import com.man.OrderService.response.ProductGetByIdResponse;
import com.man.OrderService.response.UserGetByUsernameResponse;
import com.man.OrderService.service.impl.OrderServiceImpl;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class OrderServiceTests {

	@Mock
	private CartClient cartClient;

	@Mock
	private ProductClient productClient;

	@Mock
	private UserClient userClient;

	@Mock
	private OrderRepository orderRepository;
	
	private ModelMapper mapper;

	@InjectMocks
	private OrderServiceImpl impl;
	
	

	@BeforeEach
	void setUp() {
		mapper = new ModelMapper();
		impl = new OrderServiceImpl(orderRepository, mapper, productClient,
				userClient, cartClient);

	}

	@Test
	void getByIdTest() {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("testuser", null,
				List.of());
		SecurityContextHolder.getContext().setAuthentication(token);

		UserGetByUsernameResponse user = new UserGetByUsernameResponse(101L, token.getName(), "User");

		Order order = new Order(1L, user.getId(), 60000L, "PROCESS", LocalDateTime.now(),
				List.of(new OrderDetail(1L, 200L, 1L, 10000L, 0L, null),
						new OrderDetail(1L, 201L, 1L, 20000L, 0L, null),
						new OrderDetail(1L, 202L, 1L, 30000L, 0L, null)));

		Long orderId = 1L;
		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

		Order result = impl.getById(orderId);

		assertNotNull(result);
		assertEquals(3, result.getItems().size());
		assertEquals(60000L, result.getTotalAmount());

		verify(orderRepository, times(1)).findById(orderId);
	}

	@Test
	void checkoutTest() {
		Long cartId = 1L;
		GetByIdResponse response = new GetByIdResponse(1L, 100L,
				List.of(new CartItemResponse(cartId, 200L, "Product 1", 1L, 0L),
						new CartItemResponse(cartId, 201L, "Product 2", 2L, 0L),
						new CartItemResponse(cartId, 202L, "Product 3", 3L, 0L)),
				0L);

		when(cartClient.getById(cartId)).thenReturn(response);

		when(productClient.getById(200L)).thenReturn(new ProductGetByIdResponse(200L, "Product 1", 10L, 10000L));
		when(productClient.getById(201L)).thenReturn(new ProductGetByIdResponse(201L, "Product 2", 10L, 20000L));
		when(productClient.getById(202L)).thenReturn(new ProductGetByIdResponse(202L, "Product 3", 0L, 30000L));

		CheckoutResponse result = impl.checkout(cartId);
		assertNotNull(result);
		assertEquals(50000, result.getTotalPaid());
		assertEquals(3, result.getItems().size());
		assertEquals("OUT_OF_STOCk", result.getItems().get(2).getStatus());
		assertEquals("Ok", result.getItems().get(0).getStatus(), result.getItems().get(1).getStatus());

		verify(cartClient, times(1)).getById(anyLong());
		verify(cartClient, times(1)).clearCart(cartId);
		verify(productClient, times(3)).getById(anyLong());
	}
	

	@Test
	void getByUserIdTest() {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("testuser", null,
				List.of());
		SecurityContextHolder.getContext().setAuthentication(token);

		UserGetByUsernameResponse user = new UserGetByUsernameResponse(101L, token.getName(), "User");

		Cart cart = new Cart(1L, user.getId(), List.of(
				new CartItem(100L, null, 200L, 1L, 0L),
				new CartItem(100L, null, 201L, 1L, 0L), 
				new CartItem(100L, null, 202L, 1L, 0L)), 0L);

		when(userClient.getByUsername(anyString())).thenReturn(user);
		when(cartClient.getByUserId(anyLong())).thenReturn(List.of(cart));
		
		when(productClient.getById(200L)).thenReturn(new ProductGetByIdResponse(200L, "Product 1", 10L, 10000L));
		when(productClient.getById(201L)).thenReturn(new ProductGetByIdResponse(201L, "Product 2", 10L, 20000L));
		when(productClient.getById(202L)).thenReturn(new ProductGetByIdResponse(202L, "Product 3", 0L, 30000L));
		
		List<OrderResponse> result = impl.getByUserId(user.getUsername());
		assertNotNull(result);
		assertEquals(3L, result.get(0).getItems().size());
		assertEquals(60000L, result.get(0).getTotalAmount());
		
		verify(userClient, times(1)).getByUsername(anyString());
		verify(cartClient, times(1)).getByUserId(anyLong());
		verify(productClient, times(3)).getById(anyLong());
	}

}
