package com.man.OrderService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
		security.csrf().disable()
			.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
			.headers(header -> header.frameOptions(frame -> frame.disable())).logout(logout -> logout.disable());
		return security.build();

	}
}
