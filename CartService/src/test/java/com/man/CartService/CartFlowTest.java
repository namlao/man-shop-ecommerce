package com.man.CartService;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.man.CartService.entity.Cart;
import com.man.CartService.entity.CartItem;
import com.man.CartService.repository.CartRepository;
import com.man.CartService.repository.ItemCartRepository;
import com.man.CartService.response.CartItemResponse;
import com.man.CartService.response.UserGetByUsernameResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"user-service.url=http://localhost:${wiremock.server.port}",
		"product-service.url=http://localhost:${wiremock.server.port}" })
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CartFlowTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ItemCartRepository itemCartRepository;

	@RegisterExtension
	static WireMockExtension wiremock = WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();

	@DynamicPropertySource
	static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("product-service.url", () -> wiremock.baseUrl() + "/product");
		registry.add("user-service.url", wiremock::baseUrl);
	}

	@Test
	@DirtiesContext
	void createFlowTest() throws Exception {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("testuser", null,
				List.of());
		SecurityContextHolder.getContext().setAuthentication(token);
		UserGetByUsernameResponse user = new UserGetByUsernameResponse(101L, token.getName(), "User");
		Long userId = user.getId();

		wiremock.stubFor(get(urlEqualTo("/u/testuser")).willReturn(okJson("""
				{
					"id": %d,
					"username": "testuser",
					"role": "USER"
				}
				""".formatted(userId))));

		wiremock.stubFor(get(urlEqualTo("/product/200")).willReturn(okJson("""
				{
					"id": 200,
					"name": "Product 1",
					"price": 10000,
					"quanity": 100
				}
				""")));
		wiremock.stubFor(get(urlEqualTo("/product/201")).willReturn(okJson("""
				{
					"id": 201,
					"name": "Product 2",
					"price": 15000,
					"quanity": 200
				}
				""")));

		MvcResult result = mockMvc.perform(post("/create").contentType(MediaType.APPLICATION_JSON).content("""
					{
						"userId": %d,
						"items": [
									{
										"productId": 200,
										"quantity": 50,
										"subTotal": 0
									},
									{
										"productId": 201,
										"quantity": 30,
										"subTotal": 0
									}
								]
					}
				""".formatted(userId))).andExpect(status().isOk()).andReturn();

		String responseJson = result.getResponse().getContentAsString();
		JsonNode root = new ObjectMapper().readTree(responseJson);

		Long userIdTest = root.get("userId").asLong();
		Long total = root.get("total").asLong();
		JsonNode itemNode = root.get("items");

		Long totalTest = 0L;
		List<CartItemResponse> cartItem = new ArrayList<>();
		for (JsonNode jsonNode : itemNode) {
			Long productId = jsonNode.get("productId").asLong();
			Long quantity = jsonNode.get("quantity").asLong();
			Long subTotal = jsonNode.get("subTotal").asLong();
			cartItem.add(new CartItemResponse(0L, productId, quantity, subTotal));
			totalTest += subTotal;
		}

		assertNotNull(userIdTest);
		assertNotNull(total);
		assertEquals(2, cartItem.size());
		assertEquals(total, totalTest);
		assertEquals(200, cartItem.get(0).getProductId());
		assertEquals(201, cartItem.get(1).getProductId());

		Optional<Cart> saved = Optional.of(cartRepository.findByUserId(userId).get(0));
		assertTrue(saved.isPresent());
		assertEquals(saved.get().getUserId(), 101);

	}

	@Test
	@DirtiesContext
	void addProductToCartFlowTest() throws Exception {
		Cart cart = new Cart(null, 100L, new ArrayList<>(), 0L);

		cart.addCartItem(new CartItem(null, null, 300L, 10L, 20000L));
		cart.addCartItem(new CartItem(null, null, 301L, 2L, 10000L));

		cartRepository.save(cart);

		wiremock.stubFor(get(urlEqualTo("/product/300")).willReturn(okJson("""
				{
					"id": 300,
					"name": "Product 1",
					"price": 10000,
					"quanity": 5
				}
				""")));
		wiremock.stubFor(get(urlEqualTo("/product/301")).willReturn(okJson("""
				{
					"id": 301,
					"name": "Product 2",
					"price": 15000,
					"quanity": 200
				}
				""")));
		wiremock.stubFor(get(urlEqualTo("/product/302")).willReturn(okJson("""
				{
					"id": 302,
					"name": "Product 3",
					"price": 15000,
					"quanity": 200
				}
				""")));

		MvcResult result = mockMvc.perform(post("/addProduct").contentType(MediaType.APPLICATION_JSON).content("""
					{
						"id": 1,
						"items": [
									{
										"productId": 300,
										"quantity": 50,
										"subTotal": 0
									},
									{
										"productId": 302,
										"quantity": 30,
										"subTotal": 0
									}
								]
					}
				""")).andExpect(status().isOk()).andReturn();

		String responseJson = result.getResponse().getContentAsString();
		JsonNode root = new ObjectMapper().readTree(responseJson);

		Long cartId = root.get("id").asLong();
		Long userId = root.get("userId").asLong();
		Long total = root.get("total").asLong();

		assertNotNull(cartId);
		assertNotNull(userId);
		assertNotNull(total);
		assertEquals(950000, total);

		Optional<Cart> savedCart = cartRepository.findById(1L);
		assertTrue(savedCart.isPresent());

		JsonNode node = root.get("items");
		assertNotNull(total);

		List<CartItem> cartItems = new ArrayList<>();
		Long totalNode = 0L;
		for (JsonNode jsonNode : node) {
			Long id = jsonNode.get("id").asLong();
			Long productId = jsonNode.get("productId").asLong();
			Long quantity = jsonNode.get("quantity").asLong();
			Long subTotal = jsonNode.get("subTotal").asLong();
			cartItems.add(new CartItem(id, cart, productId, quantity, subTotal));
			totalNode += subTotal;
		}
		assertEquals(totalNode, 980000);
		assertEquals(3, node.size());
	}

	@Test
	@DirtiesContext
	void deleteProductToCartFlowTest() throws Exception {
		Cart cart = new Cart(null, 100L, new ArrayList<>(), 950000L);

		cart.addCartItem(new CartItem(null, null, 300L, 10L, 20000L));
		cart.addCartItem(new CartItem(null, null, 301L, 2L, 10000L));
		cart.addCartItem(new CartItem(null, null, 302L, 30L, 450000L));

		cart = cartRepository.save(cart);

		MvcResult result = mockMvc.perform(delete("/deleteProduct").contentType(MediaType.APPLICATION_JSON).content("""
				{
					"cartId": 1,
					"items": [
								{
									"productId": 300
								},
								{
									"productId": 302
								}
							]
				}
				""")).andExpect(status().isOk()).andReturn();

		String responseJson = result.getResponse().getContentAsString();
		System.out.println(responseJson);
		JsonNode root = new ObjectMapper().readTree(responseJson);
		Long id = root.get("id").asLong();
		Long total = root.get("total").asLong();

		assertNotNull(id);
		assertNotNull(total);
		assertEquals(480000, total);
		JsonNode node = root.get("items");
		assertEquals(1, node.size());

		Optional<CartItem> cartItemSaved = itemCartRepository.findByCartId(id);
		assertTrue(cartItemSaved.isPresent());

	}

	@Test
	@DirtiesContext
	void clearCartFlowTest() throws Exception {
		Cart cart = new Cart(null, 100L, new ArrayList<>(), 950000L);

		cart.addCartItem(new CartItem(null, null, 300L, 10L, 20000L));
		cart.addCartItem(new CartItem(null, null, 301L, 2L, 10000L));
		cart.addCartItem(new CartItem(null, null, 302L, 30L, 450000L));

		cart = cartRepository.save(cart);

		MvcResult result = mockMvc.perform(delete("/" + cart.getId())).andExpect(status().isOk()).andReturn();

		String responseJson = result.getResponse().getContentAsString();
		assertTrue(responseJson.isEmpty());

	}

	@Test
	@DirtiesContext
	void getByUserIdFlowTest() throws Exception {
		Cart cartUser1 = new Cart(null, 100L, new ArrayList<>(), 950000L);
		cartUser1 = cartRepository.save(cartUser1);

		Cart cartUser2 = new Cart(null, 101L, new ArrayList<>(), 950000L);
		cartUser2 = cartRepository.save(cartUser2);

		MvcResult result = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.get("/user/" + cartUser2.getUserId())).andExpect(status().isOk()).andReturn();

		String responseJson = result.getResponse().getContentAsString();
		System.out.println(responseJson);
		JsonNode node = new ObjectMapper().readTree(responseJson).get(0);
		Long id = node.get("id").asLong();
		Long userId = node.get("userId").asLong();

		assertNotNull(id);
		assertNotNull(userId);

		assertNotEquals(cartUser1.getUserId(), userId);

	}

	@Test
	@DirtiesContext
	void getByIdFlowTest() throws Exception {
		Cart cart = new Cart(null, 100L, new ArrayList<>(), 950000L);
		cart.addCartItem(new CartItem(null, null, 300L, 10L, 20000L));
		cart.addCartItem(new CartItem(null, null, 301L, 2L, 10000L));
		cart.addCartItem(new CartItem(null, null, 302L, 30L, 450000L));
		cart = cartRepository.save(cart);

		MvcResult result = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.get("/" + cart.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		System.out.println(responseJson);
		
		JsonNode node = new ObjectMapper().readTree(responseJson);
		Long id = node.get("id").asLong();
		Long userId = node.get("userId").asLong();
		assertNotNull(id);
		assertNotNull(userId);
		
		JsonNode itemNode = node.get("items");
		assertEquals(3, itemNode.size());
		
	}

}
