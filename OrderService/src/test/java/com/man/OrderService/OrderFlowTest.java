package com.man.OrderService;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
import com.man.OrderService.response.UserGetByUsernameResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"user-service.url=http://localhost:${wiremock.server.port}",
		"product-service.url=http://localhost:${wiremock.server.port}",
		"cart-service.url=http://localhost:${wiremock.server.port}" })
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderFlowTest {
	@Autowired
	private MockMvc mockMvc;

	@RegisterExtension
	static WireMockExtension wiremock = WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();

	@DynamicPropertySource
	static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("product-service.url", () -> wiremock.baseUrl() + "/product");
		registry.add("cart-service.url", () -> wiremock.baseUrl() + "/cart");
		registry.add("user-service.url", () -> wiremock.baseUrl() + "/user");
	}

	@Test
	void checkoutFlowTest() throws Exception {
		Long cartId = 1L;

		// Stub CartService trả về giỏ hàng có 2 sản phẩm
		wiremock.stubFor(get(urlEqualTo("/cart/" + cartId)).willReturn(okJson("""
				    {
				        "id": %d,
				        "userId": 1,
				        "items": [
				            {
				                "id": 1,
				                "productId": 100,
				                "name": "Product 1",
				                "quantity": 2,
				                "subTotal": 10000
				            },
				            {
				                "id": 2,
				                "productId": 101,
				                "name": "Product 2",
				                "quantity": 3,
				                "subTotal": 30000
				            },
				            {
				                "id": 3,
				                "productId": 102,
				                "name": "Product 3",
				                "quantity": 5,
				                "subTotal": 30000
				            }
				        ],
				        "total": 40000
				    }
				""".formatted(cartId))));

		// Stub xóa cart
		wiremock.stubFor(delete(urlEqualTo("/cart/" + cartId)).willReturn(aResponse().withStatus(200)));

		// Stub Product 100
		wiremock.stubFor(get(urlEqualTo("/product/100")).willReturn(okJson("""
				    {
				        "id": 100,
				        "name": "Product 1",
				        "quanity": 100,
				        "price": 5000
				    }
				""")));

		// Stub Product 101
		wiremock.stubFor(get(urlEqualTo("/product/101")).willReturn(okJson("""
				    {
				        "id": 101,
				        "name": "Product 2",
				        "quanity": 2,
				        "price": 10000
				    }
				""")));

		wiremock.stubFor(get(urlEqualTo("/product/102")).willReturn(okJson("""
				    {
				        "id": 102,
				        "name": "Product 3",
				        "quanity": 100,
				        "price": 10000
				    }
				""")));

		// Stub decreaseStock
		wiremock.stubFor(put(urlEqualTo("/product/decreaseStock")).willReturn(okJson("""
				    {
				        "id": 100,
				        "name": "Product 1",
				        "price": 5000,
				        "quanity": 98
				    }
				""")));

		// Call API checkout
		MvcResult result = mockMvc
				.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/checkout/" + cartId))
				.andExpect(status().isOk()).andReturn();

		String responseJson = result.getResponse().getContentAsString();
		System.out.println("Checkout response: " + responseJson);

		JsonNode root = new ObjectMapper().readTree(responseJson);

		Long cartIdResp = root.get("cartId").asLong();
		Long totalPaid = root.get("totalPaid").asLong();

		assertEquals(cartId, cartIdResp);
		assertTrue(totalPaid > 0);

		// Kiểm tra items trong response
		JsonNode itemsNode = root.get("items");
		assertEquals(3, itemsNode.size());
		assertEquals(100, itemsNode.get(0).get("productId").asLong());
		assertEquals(101, itemsNode.get(1).get("productId").asLong());
		assertEquals(102, itemsNode.get(2).get("productId").asLong());

		// Verify Feign thực sự gọi sang product-service decreaseStock
		wiremock.verify(putRequestedFor(urlEqualTo("/product/decreaseStock")));
	}

	@Test
	@DirtiesContext
	void listByUserIdTest() throws Exception {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("testuser", null,
				List.of());
		SecurityContextHolder.getContext().setAuthentication(token);

		UserGetByUsernameResponse user = new UserGetByUsernameResponse(101L, token.getName(), "User");
		String username = user.getUsername();
		Long userId = user.getId();

		// stub get user by username
		wiremock.stubFor(get(urlEqualTo("/user/u/" + username)).willReturn(okJson("""
				{
				    "id": %d,
				    "username": "%s",
				    "role": "USER"
				}
				""".formatted(userId, username))));

		// stub get cart by userId
		wiremock.stubFor(get(urlEqualTo("/cart/user/" + userId)).willReturn(okJson("""
				[
				    {
				        "id":200,
				        "userId": %d,
				        "items": [
				                {
				                    "id": 1,
				                    "productId": 100,
				                    "quantity": 2,
				                    "subTotal": 10000
				                },
				                {
				                    "id": 2,
				                    "productId": 101,
				                    "quantity": 2,
				                    "subTotal": 10000
				                }
				            ],
				        "total":20000
				    }
				]
				""".formatted(userId))));

		wiremock.stubFor(get(urlEqualTo("/product/100")).willReturn(okJson("""
				    {
				        "id": 100,
				        "name": "Product 1",
				        "quanity": 100,
				        "price": 5000
				    }
				""")));

		// Stub Product 101
		wiremock.stubFor(get(urlEqualTo("/product/101")).willReturn(okJson("""
				    {
				        "id": 101,
				        "name": "Product 2",
				        "quanity": 2,
				        "price": 10000
				    }
				""")));

		MvcResult result = mockMvc
				.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/listByUserId")
						.contentType(MediaType.APPLICATION_JSON)) // .with(user(username).roles("USER"))
				.andExpect(status().isOk()).andReturn();
		String responseJson = result.getResponse().getContentAsString();
		JsonNode root = new ObjectMapper().readTree(responseJson);

		// Đảm bảo là mảng
		assertTrue(root.isArray());
		assertEquals(1, root.size());

		// Order đầu tiên
		JsonNode orderNode = root.get(0);
		assertEquals(200, orderNode.get("id").asInt());
		assertEquals(101, orderNode.get("userId").asInt());
		assertEquals(30000, orderNode.get("totalAmount").asInt());
		assertEquals("PENDING", orderNode.get("status").asText());

		// Items
		JsonNode itemsNode = orderNode.get("items");
		assertTrue(itemsNode.isArray());
		assertEquals(2, itemsNode.size());

		// Item 1
		JsonNode item1 = itemsNode.get(0);
		assertEquals(100, item1.get("productId").asInt());
		assertEquals("Product 1", item1.get("productName").asText());
		assertEquals(2, item1.get("quantity").asInt());
		assertEquals(5000, item1.get("price").asInt());
		assertEquals(10000, item1.get("subTotal").asInt());

		// Item 2
		JsonNode item2 = itemsNode.get(1);
		assertEquals(101, item2.get("productId").asInt());
		assertEquals("Product 2", item2.get("productName").asText());
		assertEquals(2, item2.get("quantity").asInt());
		assertEquals(10000, item2.get("price").asInt());
		assertEquals(20000, item2.get("subTotal").asInt());
	}

}
