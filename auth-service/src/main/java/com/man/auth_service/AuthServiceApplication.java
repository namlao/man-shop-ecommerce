package com.man.auth_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.man.auth_service.entity.UserEntity;
import com.man.auth_service.repository.UserRepository;

@SpringBootApplication
@EnableDiscoveryClient
//@ComponentScan(basePackages = "com.man.auth_service")
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder encoder) {
	    return args -> {
	        if (userRepository.findByUsername("admin").isEmpty()) {
	            UserEntity user = new UserEntity();
	            user.setUsername("admin");
	            user.setPassword(encoder.encode("123"));
	            user.setRole("ROLE_ADMIN");
	            userRepository.save(user);
	        }
	        
	        if (userRepository.findByUsername("user").isEmpty()) {
	            UserEntity user = new UserEntity();
	            user.setUsername("user");
	            user.setPassword(encoder.encode("123"));
	            user.setRole("ROLE_USER"); 
	            userRepository.save(user);
	        }
	    };
	}

}


