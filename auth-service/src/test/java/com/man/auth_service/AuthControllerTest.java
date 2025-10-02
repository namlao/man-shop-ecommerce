package com.man.auth_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.man.auth_service.client.UserClient;
import com.man.auth_service.controller.AuthController;
import com.man.auth_service.entity.RefreshToken;
import com.man.auth_service.response.UserGetByIdResponse;
import com.man.auth_service.response.UserGetByUsernameResponse;
import com.man.auth_service.service.RefreshTokenService;
import com.man.auth_service.utils.JwtUtil;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class) 
class AuthControllerTest {

    @Mock
    private UserClient client;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController; // để Mockito tự inject

    @Test
    void loginTest() {
        Map<String, String> request = Map.of("username", "testunit", "password", "123456");

        // giả lập user client trả về user hợp lệ
        UserGetByUsernameResponse response =
                new UserGetByUsernameResponse(100L, "testunit", "123456", "USER");
        when(client.getByUsername("testunit")).thenReturn(response);

        // giả lập password đúng
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);

        // giả lập refresh token
        RefreshToken rf = new RefreshToken(1L, "token-test", 100L,
                Instant.now().plusSeconds(3600), false);
        when(refreshTokenService.create(100L)).thenReturn(rf);

        // giả lập jwt
        String accesstoken = "access-token-test";
        when(jwtUtil.generateToken("testunit", "USER")).thenReturn(accesstoken);

        // gọi controller
        Map<String, Object> result = authController.login(request);

        // assert
        assertNotNull(result);
        assertEquals(accesstoken, result.get("accessToken"));
        assertEquals("token-test", result.get("refreshToken"));

        // verify
        verify(client, times(1)).getByUsername("testunit");
        verify(passwordEncoder, times(1)).matches("123456", "123456");
        verify(refreshTokenService, times(1)).create(100L);
        verify(jwtUtil, times(1)).generateToken("testunit", "USER");
    }
    
    @Test
    void refreshTest() {
    	Map<String, String> request = Map.of("refreshToken", "refresh-test");
//    	String refreshToken = "refresh-test";
    	when(refreshTokenService.getUserByToken(request.get("refreshToken"))).thenReturn(101L);
    	
    	UserGetByIdResponse resp = new UserGetByIdResponse(101L, "testunit", "123456", "USER");
    	when(client.getById(anyLong())).thenReturn(resp);
    	
    	RefreshToken rf = new RefreshToken(1L, "refresh-token-test", 100L,
                Instant.now().plusSeconds(3600), false);
    	when(refreshTokenService.rotate(request.get("refreshToken"))).thenReturn(rf);
    	
    	String accessToken = "rotate-access-token-test";
    	when(jwtUtil.generateToken("testunit", "USER")).thenReturn(accessToken);
    	
    	Map<String, Object> result = authController.refresh(request);
    	
    	
    	assertNotNull(result);
    	assertEquals(accessToken, result.get("accessToken"));
    	assertEquals(rf.getToken(),  result.get("refreshToken"));
    	
    	verify(refreshTokenService,times(1)).getUserByToken(request.get("refreshToken"));
    	verify(client,times(1)).getById(101L);
    	verify(refreshTokenService,times(1)).rotate(request.get("refreshToken"));
    	verify(jwtUtil,times(1)).generateToken("testunit", "USER");
    	
    	
    }
    
    @Test
    void logoutTest() {
    	Map<String, String> request = Map.of("username", "testunit");
    	
    	UserGetByUsernameResponse user = new UserGetByUsernameResponse(103L, "testunit", "123456", "USER");
    	when(client.getByUsername(anyString())).thenReturn(user);
    	
    	Map<String, String> result = authController.logout(request);
    	
    	assertNotNull(result);
    	assertEquals(result.get("message"), "Logged out");
    	
    	verify(client, times(1)).getByUsername("testunit");
    }
}