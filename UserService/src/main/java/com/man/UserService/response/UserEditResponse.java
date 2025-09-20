package com.man.UserService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEditResponse {
	
	private Long id;
	private String name;

	private String username;

	private String password;

	private String email;

	private String phone;

	private String role;
}
