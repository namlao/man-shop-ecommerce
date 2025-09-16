package com.man.auth_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.man.auth_service.entity.RefreshToken;
import com.man.auth_service.repository.RefreshTokenReposittory;
import com.man.auth_service.service.RefreshTokenService;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTests {

	@Mock
	private RefreshTokenReposittory refreshTokenReposittory;

	@InjectMocks
	private RefreshTokenService refreshTokenService;

	public AuthServiceTests() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createTokenTest() {
		Long userId = 1L;
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setId(100L);
		refreshToken.setUserId(userId);
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setExpiryDate(Instant.now().plusSeconds(3600));

		when(refreshTokenReposittory.save(any(RefreshToken.class))).thenReturn(refreshToken);

		RefreshToken result = refreshTokenService.create(userId);
		assertNotNull(result);
		assertEquals(userId, result.getUserId());
		assertNotNull(result.getToken());

		verify(refreshTokenReposittory, times(1)).save(any(RefreshToken.class));
	}

	@Test
	void rotateTokenTest() {
		String oldToken = "old-token";
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setId(101L);
		refreshToken.setUserId(1L);
		refreshToken.setToken(oldToken);
		refreshToken.setExpiryDate(Instant.now().plusSeconds(3600));

		when(refreshTokenReposittory.findByToken(oldToken)).thenReturn(Optional.of(refreshToken));
		when(refreshTokenReposittory.save(any(RefreshToken.class))).thenAnswer(inv -> inv.getArgument(0));

		RefreshToken result = refreshTokenService.rotate(oldToken);
		assertNotNull(result.getToken());
		assertNotEquals(oldToken, result.getToken());
		verify(refreshTokenReposittory).findByToken(oldToken);
		verify(refreshTokenReposittory).save(refreshToken);

	}

	@Test
	void revokeAllByUserIdTest() {
		Long userId = 1L;
		refreshTokenService.revokeAllByUserId(userId);

		verify(refreshTokenReposittory, times(1)).deleteByUserId(userId);

	}

}
