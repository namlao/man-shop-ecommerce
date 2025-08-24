package com.man.UserService.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEditResponse {
	
	private Long id;
	private String name;

	private String username;

	private String password;

	private String email;

	private String phone;

	private String role;
}
