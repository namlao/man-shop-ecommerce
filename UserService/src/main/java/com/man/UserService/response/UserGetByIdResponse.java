package com.man.UserService.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGetByIdResponse {
	private Long id;
	private String username;
	private String password;
	private String role;
}
