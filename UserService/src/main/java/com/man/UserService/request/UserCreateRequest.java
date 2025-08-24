package com.man.UserService.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
	private String name;

	private String username;

	private String password;

	private String email;

	private String phone;

	private String role;
}
