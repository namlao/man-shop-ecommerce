package com.man.UserService.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.man.UserService.enity.User;
import com.man.UserService.exception.UserNotFoundException;
import com.man.UserService.repository.UserRepository;
import com.man.UserService.request.UserCreateRequest;
import com.man.UserService.request.UserEditRequest;
import com.man.UserService.request.UserGetByIdRequest;
import com.man.UserService.request.UserGetByUsernameRequest;
import com.man.UserService.response.UserCreateResponse;
import com.man.UserService.response.UserEditResponse;
import com.man.UserService.response.UserGetByIdResponse;
import com.man.UserService.response.UserGetByUsernameResponse;
import com.man.UserService.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public List<User> userList() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public UserCreateResponse create(UserCreateRequest req) {
		// TODO Auto-generated method stub
		User user = mapper.map(req, User.class);
		user.setPassword(encoder.encode(req.getPassword()));
		
		repository.save(user);
		
		return mapper.map(user, UserCreateResponse.class);
	}

	@Override
	public UserEditResponse edit(UserEditRequest userReq) {
		// TODO Auto-generated method stub
		User user = repository.findById(userReq.getId())
				.orElseThrow(() -> new RuntimeException("User not found with id: " + userReq.getId()));
		
		if(userReq.getEmail() != null) {
			user.setEmail(userReq.getEmail());
		}
		
		if(userReq.getName() != null) {
			user.setName(userReq.getName());
		}
		
		if(userReq.getPhone() != null) {
			user.setPhone(userReq.getPhone());
		}
		
		if(userReq.getPassword() != null) {
			user.setPassword(encoder.encode(userReq.getPassword()));
		}
		
		if(userReq.getRole() != null) {
			user.setRole(userReq.getRole());
		}
		user = repository.save(user);
//		mapper.map(userReq, User.class)
		return mapper.map(user, UserEditResponse.class);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		User user = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + id));
		repository.delete(user);

	}

	@Override
	public User getById(Long id) {
		User user = repository.findById(id)
				.orElseThrow(()->new UserNotFoundException("User not found with id "+ id));
		return user;
	}

	@Override
	public UserGetByUsernameResponse findByUsername(UserGetByUsernameRequest userReq) {
		// TODO Auto-generated method stub
		User user = repository.findByUsername(userReq.getUsername()).orElseThrow(()->new RuntimeException("User Not found with username: "+userReq.getUsername()));
		return mapper.map(user, UserGetByUsernameResponse.class);
	}
	
	@Override
	public UserGetByIdResponse findById(UserGetByIdRequest userRq) {
		User user = repository.findById(userRq.getId())
				.orElseThrow(() -> new UserNotFoundException("User not found with id " + userRq.getId()));
		return mapper.map(user, UserGetByIdResponse.class);
	}

}
