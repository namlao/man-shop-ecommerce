package com.man.UserService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.man.UserService.enity.User;
import com.man.UserService.repository.UserRepository;

@Component
@Configuration
public class PushData {

	private final PasswordEncoder encoder;
	private final UserRepository repo;

	public PushData(PasswordEncoder encoder,UserRepository repo) {
		this.encoder = encoder;
		this.repo = repo;
	}

	@Bean
	public User initUser() {
		User user = new User();
		user.setUsername("admin");
		user.setName("admin");
		user.setPassword(encoder.encode("123456"));
		user.setEmail("admin@email.com");
		user.setRole("ROLE_ADMIN");
		return repo.save(user);
	}
}
