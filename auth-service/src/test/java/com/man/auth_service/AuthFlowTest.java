package com.man.auth_service;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.man.auth_service.entity.RefreshToken;
import com.man.auth_service.repository.RefreshTokenReposittory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthFlowTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RefreshTokenReposittory refreshTokenReposittory;

	// WireMock sẽ tự start/stop cho bạn
	@RegisterExtension
	static WireMockExtension wiremock = WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();

	@DynamicPropertySource
	static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("user-service.url", wiremock::baseUrl);
	}


	@Test
	@DirtiesContext
	void loginFlowITest() throws Exception {
		String encoded = new BCryptPasswordEncoder().encode("123456");
		// Stub API user-service
		wiremock.stubFor(get(urlEqualTo("/u/testunit")).willReturn(okJson("""
				{
				  "id": 101,
				  "username": "testunit",
				  "password": "%s",
				  "role": "USER"
				}
				""".formatted(encoded))));

		// Gọi login API

		MvcResult result = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "username": "testunit",
				  "password": "123456"
				}
				""")).andExpect(status().isOk()).andReturn();

		String responseJson = result.getResponse().getContentAsString();
//		System.out.println("Login response: " + responseJson);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(responseJson);

		String accessToken = node.get("accessToken").asText();
		String refreshToken = node.get("refreshToken").asText();

		assertNotNull(accessToken);
		assertNotNull(refreshToken);

		// Kiểm tra token lưu DB
		Optional<RefreshToken> saved = refreshTokenReposittory.findByToken(refreshToken);
		assertTrue(saved.isPresent(), "RefreshToken đã được lưu");
		assertEquals(101L, saved.get().getUserId());
	}

	//
	@Test
	@DirtiesContext
	void refreshFlowTest() throws Exception {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken("7fe09b98-1242-4193-8e26-70efeb8399e5");
		refreshToken.setUserId(100L);
		refreshToken.setExpiryDate(Instant.now().plusSeconds(3600));
		refreshTokenReposittory.save(refreshToken);

		wiremock.stubFor(get(urlEqualTo("/i/100")).willReturn(okJson("""
				{
					"id": 100,
					"username": "testunit",
					"role": "USER"
				}
				""")));

		String rf = refreshToken.getToken();

		MvcResult result = mockMvc.perform(post("/refresh").contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "refreshToken": "%s"
				}
				""".formatted(rf))).andExpect(status().isOk()).andReturn();

		String reponseJson = result.getResponse().getContentAsString();
		System.out.println("Refresh Token Response: " + reponseJson);

		JsonNode node = new ObjectMapper().readTree(reponseJson);
		String newAccess = node.get("accessToken").asText();
		String newRefresh = node.get("refreshToken").asText();

		RefreshToken oldToken = refreshTokenReposittory.findByToken("7fe09b98-1242-4193-8e26-70efeb8399e5")
				.orElseThrow();

		assertNotNull(newAccess);
		assertNotNull(newRefresh);

		assertTrue(refreshTokenReposittory.findByToken(newRefresh).isPresent());
		assertTrue(oldToken.isRevoked(), "Old refresh token should be revoked");

	}

	@Test
	@DirtiesContext
	void logoutFlowTest() throws Exception {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken("7fe09b98-1242-4193-8e26-70efeb8399e5");
		refreshToken.setUserId(100L);
		refreshToken.setExpiryDate(Instant.now().plusSeconds(3600));
		refreshTokenReposittory.save(refreshToken);

		String encoded = new BCryptPasswordEncoder().encode("123456");
		wiremock.stubFor(get(urlEqualTo("/u/testunit")).willReturn(okJson("""
				{

				  "id": 100,
				  "username": "testunit",
				  "password": "%s",
				  "role": "USER"

				}
				""".formatted(encoded))));

		MvcResult result = mockMvc.perform(post("/logout").contentType(MediaType.APPLICATION_JSON).content("""
				{
				"username": "testunit"
				}
				""")).andExpect(status().isOk()).andReturn();

		
		String responseJson = result.getResponse().getContentAsString();
		JsonNode node = new ObjectMapper().readTree(responseJson);
		
		String messageNode = node.get("message").asText();
		assertNotNull(messageNode);
		
		Optional<RefreshToken> deleted = refreshTokenReposittory.findByToken("7fe09b98-1242-4193-8e26-70efeb8399e5");
		
		
		assertFalse(deleted.isPresent(),"Token đã được xóa");
	}
	
}
