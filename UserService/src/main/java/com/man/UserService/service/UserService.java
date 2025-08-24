package com.man.UserService.service;

import java.util.List;

import com.man.UserService.enity.User;
import com.man.UserService.request.UserCreateRequest;
import com.man.UserService.request.UserEditRequest;
import com.man.UserService.response.UserCreateResponse;
import com.man.UserService.response.UserEditResponse;

public interface UserService {
	List<User> userList();
	User getById(Long id);
	List<User> findByUsername(String username);
	UserCreateResponse create(UserCreateRequest req);
	UserEditResponse edit(UserEditRequest user);
	void delete(Long id);

}
