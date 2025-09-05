package com.man.OrderService.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGetByUsernameResponse {
	private Long id;
	private String username;
	private String role;

}
