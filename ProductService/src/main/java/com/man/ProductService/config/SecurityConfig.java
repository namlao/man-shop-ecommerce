package com.man.ProductService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
		security.csrf().disable()
		.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
		.headers(headers -> headers.frameOptions(frame -> frame.disable()))
		.logout(logout -> logout.disable())
		.addFilterBefore(new RoleHeaderFilter(), UsernamePasswordAuthenticationFilter.class);
		
		return security.build();
	}
}
