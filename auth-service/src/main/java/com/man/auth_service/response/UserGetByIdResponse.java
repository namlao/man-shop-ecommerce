package com.man.auth_service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserGetByIdResponse {
	private Long id;
	private String username;
	private String password;
	private String role;
}
