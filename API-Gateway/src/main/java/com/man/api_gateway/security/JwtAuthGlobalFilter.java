package com.man.api_gateway.security;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.ws.rs.core.HttpHeaders;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthGlobalFilter implements GlobalFilter, Ordered {
	private final JwtValidator jwtValidator;
	private final AntPathMatcher pathMatcher = new AntPathMatcher();
//	private final JwtUtil jwtUtil;

	private static final List<String> WHiTELIST = List.of("/auth/login", "/auth/refresh", "/auth/logout",
			"/actuator/**", "/eureka/**", "/users/create", "/cart/**"

	);

	public JwtAuthGlobalFilter(JwtValidator jwtValidator) {
		this.jwtValidator = jwtValidator;
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// TODO Auto-generated method stub
		String path = exchange.getRequest().getURI().getPath();

		if (isWhiteListed(path)) {
			return chain.filter(exchange);
		}

		String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return unauthorized(exchange, "Missing or invalid Authorization header");
		}
		String token = authHeader.substring(7);

//		Claims claims;

		try {
			jwtValidator.parseClaims(token);
		} catch (JwtException e) {
			// TODO: handle exception
			return unauthorized(exchange, "Invalid or expired token");
		}

//		String username = jwtValidator.getUsername(claims).orElse("");
//		String role = jwtValidator.getRole(claims).orElse("USER");
//
//		ServerHttpRequest request = exchange.getRequest().mutate().header("X-Auth-Users", username)
//				.header("X-Auth-Roles", role).build();

//		return chain.filter(exchange.mutate().request(request).build());
		return chain.filter(exchange);
	}

	public boolean isWhiteListed(String path) {
		for (String pattern : WHiTELIST) {
			if (pathMatcher.match(pattern, path))
				return true;
		}
		return false;
	}

	public Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		byte[] bytes = ("{\"error\":\"" + message + "\"}").getBytes(StandardCharsets.UTF_8);
		return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
	}

}
