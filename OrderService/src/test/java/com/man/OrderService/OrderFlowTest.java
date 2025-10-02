package com.man.OrderService;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.man.OrderService.repository.OrderRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"user-service.url=http://localhost:${wiremock.server.port}",
		"product-service.url=http://localhost:${wiremock.server.port}",
		"cart-service.url=http://localhost:${wiremock.server.port}" })
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderFlowTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private OrderRepository orderRepository;

	@RegisterExtension
	static WireMockExtension wiremock = WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();

	@DynamicPropertySource
	static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("product-service.url",() -> wiremock.baseUrl() + "/product"); 
		registry.add("user-service.url", wiremock::baseUrl); //() -> wiremock.baseUrl() + "/user"
		registry.add("cart-service.url", () -> wiremock.baseUrl() + "/cart"); 
	}

	@Test
	mình vẫn bị như vậy
	void checkoutFlowTest() throws Exception {
		Long id = 1L;
		wiremock.stubFor(get(urlEqualTo("/cart/" + id)).willReturn(okJson("""
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
						}
					],
					"total": 40000
				}
				""".formatted(id))));
		
		wiremock.stubFor(delete(urlEqualTo("/cart/" + id))
			    .willReturn(aResponse().withStatus(200)));
		
		wiremock.stubFor(get(urlEqualTo("/product/100")).willReturn(okJson("""
					{
						"id": 100,
						"name": "Product 1",
						"quanity": 100,
						"price": 5000
					}
				""")));
		
		wiremock.stubFor(get(urlEqualTo("/product/101")).willReturn(okJson("""
				{
					"id": 101,
					"name": "Product 2",
					"quanity": 100,
					"price": 10000
				}
			""")));
		
		wiremock.stubFor(put(urlEqualTo("/product/decreaseStock")).willReturn(okJson("""
					{
						"id": 100,
						"name": "Product 1",
						"price": 5000,
						"quanity": 98
					}
				""")));

		MvcResult result = mockMvc
				.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/checkout/"+id))
				.andExpect(status().isOk()).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		System.out.println(responseJson);
		
		JsonNode root = new ObjectMapper().readTree(responseJson);

	}
	
}


//wiremock.stubFor(get(urlEqualTo("/u/testuser")).willReturn(okJson("""
//		{
//			"id": 1,
//			"username": "testuser",
//			"role": "USER"
//		}
//		""")));
//
//wiremock.stubFor(get(urlEqualTo("/"+cartId)).willReturn(okJson("""
//		{
//			"id": %d,
//			"userId": 1,
//			"items": [
//				{
//					"id": 1,
//					"productId": 100,
//					"quantity": 2,
//					"subTotal": 10000
//				},
//				{
//					"id": 2,
//					"productId": 101,
//					"quantity": 2,
//					"subTotal": 10000
//				}
//			],
//			"total": 20000
//		}
//		""".formatted(cartId))));
