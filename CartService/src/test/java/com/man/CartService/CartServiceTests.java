package com.man.CartService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import com.man.CartService.client.ProductClient;
import com.man.CartService.client.UserClient;
import com.man.CartService.entity.Cart;
import com.man.CartService.entity.CartItem;
import com.man.CartService.repository.CartRepository;
import com.man.CartService.repository.ItemCartRepository;
import com.man.CartService.request.AddProductToCartRequest;
import com.man.CartService.request.CartCreateRequest;
import com.man.CartService.request.CartItemRequest;
import com.man.CartService.request.DeleteProductToCartRequest;
import com.man.CartService.response.AddProductToCartResponse;
import com.man.CartService.response.CartCreateResponse;
import com.man.CartService.response.DeleteProductToCartResponse;
import com.man.CartService.response.GetByIdResponse;
import com.man.CartService.response.ProductGetByIdResponse;
import com.man.CartService.response.UserGetByUsernameResponse;
import com.man.CartService.service.impl.CartServiceImpl;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CartServiceTests {
	private ModelMapper mapper;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private ItemCartRepository itemCartRepositor;

	@Mock
	private UserClient userClient;

	@Mock
	private ProductClient productClient;

	@InjectMocks
	private CartServiceImpl impl;

	@BeforeEach
	void setUp() {
		mapper = new ModelMapper();
		impl = new CartServiceImpl(cartRepository, itemCartRepositor, productClient, userClient, mapper);

	}

	@Test
	void createCartTest() {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("testuser", null,
				List.of());
		SecurityContextHolder.getContext().setAuthentication(token);

		UserGetByUsernameResponse user = new UserGetByUsernameResponse(101L, token.getName(), "User");

		when(userClient.getByUsername(anyString())).thenReturn(user);

		when(productClient.getById(200L)).thenReturn(new ProductGetByIdResponse(200L, "Product 1", 10000L, 50));
		when(productClient.getById(201L)).thenReturn(new ProductGetByIdResponse(201L, "Product 2", 20000L, 6));

		List<CartItemRequest> itemRequests = List.of(new CartItemRequest(200L, 2L, 0L),
				new CartItemRequest(201L, 5L, 0L));

		CartCreateRequest request = new CartCreateRequest();
		request.setUserId(user.getId());
		request.setItems(itemRequests);

		when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));

		CartCreateResponse result = impl.createCart(request);
		assertNotNull(result);

		assertEquals(user.getId(), result.getUserId());
		assertEquals(2, result.getItems().size());
		assertEquals(2 * 10000 + 5 * 20000, result.getTotal());

		verify(userClient, times(1)).getByUsername(token.getName());
		verify(productClient, times(2)).getById(anyLong());
		verify(cartRepository, times(1)).save(any(Cart.class));

	}

	@Test
	void addProductToCartTest() {
		// Giả lập db
		Cart cart = new Cart(1L, 200L, new ArrayList<>(), 0L);

//		List<CartItem> cartItems = new ArrayList<>();
		cart.addCartItem(new CartItem(100L, null, 300L, 5L, 0L));
		cart.addCartItem(new CartItem(101L, null, 300L, 3L, 0L));
		cart.addCartItem(new CartItem(102L, null, 301L, 6L, 0L));
		cart.addCartItem(new CartItem(103L, null, 302L, 7L, 0L));

		when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
		when(productClient.getById(300L)).thenReturn(new ProductGetByIdResponse(300L, "Product 1", 10000L, 50));
		when(productClient.getById(301L)).thenReturn(new ProductGetByIdResponse(301L, "Product 2", 20000L, 20));
		when(productClient.getById(302L)).thenReturn(new ProductGetByIdResponse(302L, "Product 3", 30000L, 5));
		when(productClient.getById(303L)).thenReturn(new ProductGetByIdResponse(303L, "Product 4", 50000L, 7));

		when(itemCartRepositor.findByCartIdAndProductId(anyLong(), anyLong())).thenAnswer(inv -> {
			Long prouductId = inv.getArgument(1);

			return cart.getItems().stream().filter(i -> i.getProductId().equals(prouductId)).findFirst();
		});

		when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));

		// giả lập request
		List<CartItemRequest> itemRequests = new ArrayList<>();
		itemRequests.add(new CartItemRequest(300L, 5L, 0L));
		itemRequests.add(new CartItemRequest(301L, 20L, 0L));
		itemRequests.add(new CartItemRequest(302L, 5L, 0L));
		itemRequests.add(new CartItemRequest(303L, 5L, 0L));
		AddProductToCartRequest request = new AddProductToCartRequest();
		request.setId(cart.getId());
		request.setItems(itemRequests);

		AddProductToCartResponse result = impl.addProductToCart(request);
		// Assert
		assertNotNull(result);
		assertEquals(cart.getId(), result.getId());
		assertEquals(850000L, result.getTotal());
		assertEquals(5, result.getItems().size());

		// Verify mock interactions
		verify(cartRepository, times(1)).findById(cart.getId());
		verify(cartRepository, times(1)).save(any(Cart.class));
		verify(productClient, times(8)).getById(anyLong());

	}

	@Test
	void removeProductFromCartTest() {
		// Giả lập db
		Cart cart = new Cart(1L, 200L, new ArrayList<>(), 0L);

		cart.addCartItem(new CartItem(100L, null, 300L, 5L, 50000L));
		cart.addCartItem(new CartItem(101L, null, 301L, 3L, 30000L));
		cart.addCartItem(new CartItem(102L, null, 302L, 6L, 120000L));
		cart.addCartItem(new CartItem(103L, null, 303L, 7L, 210000L));
		cart.setTotal(410000L);

		when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));

		List<CartItem> cartItemsReq = List.of(new CartItem(null, null, 300L, null, null),
				new CartItem(null, null, 301L, null, null));

		when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));
		DeleteProductToCartRequest request = new DeleteProductToCartRequest();
		request.setCartId(cart.getId());
		request.setItems(cartItemsReq);

		DeleteProductToCartResponse result = impl.removeProductFromCart(request);
		assertNotNull(result);
		assertEquals(2, result.getItems().size());
		assertEquals(330000L, result.getTotal());

		verify(cartRepository, times(1)).findById(anyLong());
		verify(cartRepository, times(1)).save(any(Cart.class));
	}

	@Test
	void clearCartTest() {
		Cart cart = new Cart(1L, 200L, new ArrayList<>(), 0L);

		when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));

		impl.clearCart(cart.getId());
		verify(cartRepository, times(1)).delete(any(Cart.class));
	}
	
	@Test
	void getCartByUserIdTest() {
		// TODO Auto-generated method stub
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("testuser", null,
				List.of());
		SecurityContextHolder.getContext().setAuthentication(token);

		UserGetByUsernameResponse user = new UserGetByUsernameResponse(101L, token.getName(), "User");
	
		Cart cart1 = new Cart(1L, 101L, new ArrayList<>(), 0L);
		Cart cart2 = new Cart(1L, 102L, new ArrayList<>(), 0L);
		
		when(cartRepository.findByUserId(user.getId())).thenReturn(List.of(cart1));
		
		List<Cart> result = impl.getCartByUserId(user.getId());
		
		assertNotNull(result);
		assertFalse(result.contains(cart2));
		assertEquals(1, result.size());
		
		verify(cartRepository,times(1)).findByUserId(anyLong());
		
	}

	@Test
	void getByIdTest() {
		Cart cart = new Cart(1L, 101L, new ArrayList<>(), 80000L);
		cart.addCartItem(new CartItem(100L, null, 300L, 5L, 50000L));
		cart.addCartItem(new CartItem(101L, null, 301L, 3L, 30000L));
		when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
		
		GetByIdResponse result = impl.getById(1L);
		assertNotNull(result);
		assertEquals(cart.getId(), result.getId());
		assertEquals(cart.getUserId(), result.getUserId());
		assertEquals(2, result.getItems().size());
		verify(cartRepository,times(1)).findById(anyLong());
	}
}
