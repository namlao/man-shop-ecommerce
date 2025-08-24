package com.man.UserService.service;

import java.util.List;

import com.man.UserService.enity.User;
import com.man.UserService.request.UserCreateRequest;
import com.man.UserService.request.UserEditRequest;
import com.man.UserService.request.UserGetByIdRequest;
import com.man.UserService.request.UserGetByUsernameRequest;
import com.man.UserService.response.UserCreateResponse;
import com.man.UserService.response.UserEditResponse;
import com.man.UserService.response.UserGetByIdResponse;
import com.man.UserService.response.UserGetByUsernameResponse;

public interface UserService {
	List<User> userList();
	User getById(Long id);
	UserGetByIdResponse findById(UserGetByIdRequest userRq);
	UserGetByUsernameResponse findByUsername(UserGetByUsernameRequest username);
	UserCreateResponse create(UserCreateRequest req);
	UserEditResponse edit(UserEditRequest user);
	void delete(Long id);

}
