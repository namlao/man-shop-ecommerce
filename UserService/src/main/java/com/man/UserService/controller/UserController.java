package com.man.UserService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.man.UserService.enity.User;
import com.man.UserService.exception.UserNotFoundException;
import com.man.UserService.request.UserCreateRequest;
import com.man.UserService.request.UserDeleteRequest;
import com.man.UserService.request.UserEditRequest;
import com.man.UserService.request.UserGetByIdRequest;
import com.man.UserService.request.UserGetByUsernameRequest;
import com.man.UserService.response.UserCreateResponse;
import com.man.UserService.response.UserDeleteResponse;
import com.man.UserService.response.UserEditResponse;
import com.man.UserService.response.UserGetByIdResponse;
import com.man.UserService.response.UserGetByUsernameResponse;
import com.man.UserService.service.UserService;

@RestController
public class UserController {
	
	@Autowired private UserService service;
	
	@GetMapping("/list")
	public List<User> getList(){
		return service.userList();
	}
	
	@GetMapping("/u/{username}")
	public UserGetByUsernameResponse getByUsername(@PathVariable("username") String username){
		UserGetByUsernameRequest request = new UserGetByUsernameRequest();
		request.setUsername(username);
		return service.findByUsername(request);
	}
	
	@GetMapping("/i/{id}")
	public UserGetByIdResponse getById(@PathVariable("id") Long id){
		UserGetByIdRequest request = new UserGetByIdRequest();
		request.setId(id);
		return service.findById(request);
	}
	
	@PostMapping("/create")
	public UserCreateResponse create(@RequestBody UserCreateRequest userRq) {
		return service.create(userRq);
	}
	
	@PutMapping("/edit")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public UserEditResponse edit(@RequestBody UserEditRequest userRq) {
		return service.edit(userRq);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<UserDeleteResponse> delete(@RequestBody UserDeleteRequest userReq) {
		try {
			User user = service.getById(userReq.getId());
			service.delete(user.getId());

			return ResponseEntity.ok(new UserDeleteResponse(200L, "User deleted successfuly"));
		} catch (UserNotFoundException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserDeleteResponse(404L, e.getMessage()));
		}
	}
}
