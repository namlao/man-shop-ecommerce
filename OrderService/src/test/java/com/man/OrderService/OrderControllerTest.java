package com.man.OrderService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import com.man.OrderService.controller.OrderController;
import com.man.OrderService.response.CheckoutItemResponse;
import com.man.OrderService.response.CheckoutResponse;
import com.man.OrderService.response.OrderResponse;
import com.man.OrderService.response.UserGetByUsernameResponse;
import com.man.OrderService.service.OrderService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

	@InjectMocks
	private OrderController orderController;

	@Mock
	private OrderService orderService;

	@Test
	void checkoutTest() {
		Long cartId = 1L;
		CheckoutResponse response = new CheckoutResponse(cartId,
				List.of(new CheckoutItemResponse(100L, "Product 1", 2L, 5L, "OK")), 10000L);
		when(orderService.checkout(anyLong())).thenReturn(response);
		CheckoutResponse result = orderController.checkout(cartId);
		assertNotNull(result);
		
		verify(orderService,times(1)).checkout(anyLong());
		
	}
	
	@Test
	void listByUserIdTest() {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("testuser", null,
				List.of());
		SecurityContextHolder.getContext().setAuthentication(token);

		UserGetByUsernameResponse user = new UserGetByUsernameResponse(101L, token.getName(), "User");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		List<OrderResponse> result = orderController.listByUserId(authentication);
		assertNotNull(result);
		
		verify(orderService,times(1)).getByUserId(user.getUsername());
		
	}

}
