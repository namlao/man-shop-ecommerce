package com.man.UserService.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.man.UserService.enity.User;
import com.man.UserService.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PushData implements CommandLineRunner{

	private final PasswordEncoder encoder;
	private final UserRepository repo;


	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		User user = new User();
		user.setUsername("admin");
		user.setName("admin");
		user.setPassword(encoder.encode("123456"));
		user.setEmail("admin@email.com");
		user.setRole("ROLE_ADMIN");
		
		User user1 = new User();
		user1.setUsername("user");
		user1.setName("user");
		user1.setPassword(encoder.encode("123456"));
		user1.setEmail("user@email.com");
		user1.setRole("ROLE_USER");
		
		List<User> listUsers = Arrays.asList(user,user1);
		
		listUsers.forEach(item-> repo.save(item));
	}
}
