package com.man.CartService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
import com.man.CartService.request.CartCreateRequest;
import com.man.CartService.request.CartItemRequest;
import com.man.CartService.response.CartCreateResponse;
import com.man.CartService.response.UserGetByUsernameResponse;
import com.man.CartService.service.CartService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CartControllerTest {
	@Mock private CartService cartService;
	
	@InjectMocks CartController cartController;
	
//	@Test
//	void createTest() {
//		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("testuser", null,
//				List.of());
//		SecurityContextHolder.getContext().setAuthentication(token);
//
//		UserGetByUsernameResponse user = new UserGetByUsernameResponse(101L, token.getName(), "User");
//
//		List<CartItemRequest> itemRequests = List.of(
//				new CartItemRequest(100L,10L,20000L),
//				new CartItemRequest(101L,20L,30000L)
//				);
//		
//		CartCreateRequest request = new CartCreateRequest();
//		request.setUserId(user.getId());
//		request.setItems(itemRequests);
//		
//		CartCreateResponse response = new CartCreateResponse();
//
//		
//		when(cartService.createCart(any(CartCreateRequest.class))).thenReturn(response);
//		CartCreateResponse result = cartController.create(request);
//		
//		assertNotNull(result.getTotal());
////		assertEquals(user.getId(), result.getUserId());
//		assertEquals(100L, result.getTotal());
//		
//		
//	}
}
