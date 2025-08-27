package com.man.CartService.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGetByIdResponse {
	private Long id;
	private String username;
	private String role;
}
