package com.man.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.man.UserService.controller.UserController;
import com.man.UserService.enity.User;
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

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	@Test
	void getListTest() {
		List<User> users = List.of(new User(100L, "Nam", "manam", "123456", "manam@email.com", "0123456789", "Admin"),
				new User(101L, "Dong", "hadong", "123456", "hadong@email.com", "0123476789", "Admin"),
				new User(102L, "Viet", "haviet", "123456", "haviet@email.com", "0123476788", "User"));

		when(userService.userList()).thenReturn(users);

		List<User> result = userController.getList();

		assertNotNull(result);
		assertEquals(3, result.size());

		verify(userService, times(1)).userList();
	}

	@Test
	void getByUsernameTest() {
		String username = "usertest";
		UserGetByUsernameResponse response = new UserGetByUsernameResponse(100L, "usertest", "123456", "Admin");

		when(userService.findByUsername(any(UserGetByUsernameRequest.class))).thenReturn(response);

		UserGetByUsernameResponse result = userController.getByUsername(username);

		assertNotNull(result);
		assertEquals(username, result.getUsername());

		verify(userService, times(1)).findByUsername(any(UserGetByUsernameRequest.class));
	}

	@Test
	void getByIdTest() {
		Long id = 100L;
		UserGetByIdResponse response = new UserGetByIdResponse(100L, "usertest", "123456", "Admin");

		when(userService.findById(any(UserGetByIdRequest.class))).thenReturn(response);

		UserGetByIdResponse result = userController.getById(id);

		assertNotNull(result);
		assertEquals(id, result.getId());

		verify(userService, times(1)).findById(any(UserGetByIdRequest.class));
	}

	@Test
	void createTest() {
		UserCreateRequest request = new UserCreateRequest("usertest", "usertest", "123456", "test@email.com",
				"0123456789", "User");
		UserCreateResponse response = new UserCreateResponse("usertest", "usertest", "123456", "test@email.com",
				"0123456789", "User");

		when(userService.create(request)).thenReturn(response);

		UserCreateResponse result = userController.create(request);

		assertNotNull(result);

		verify(userService, times(1)).create(any(UserCreateRequest.class));
	}

	@Test
	void editTest() {
		UserEditRequest request = new UserEditRequest(100L, "usertest", "123456", "test@email.com", "0123456789",
				"User");
		UserEditResponse response = new UserEditResponse(100L, "usertest123", "usertest", "123457", "test1@email.com",
				"0123465789", "User");
		when(userService.edit(request)).thenReturn(response);

		UserEditResponse result = userController.edit(request);

		assertNotNull(result);

		verify(userService, times(1)).edit(any(UserEditRequest.class));
	}
	
	@Test
	void deleteTest() {
		Long id = 100L;
		UserDeleteRequest request = new UserDeleteRequest();
		request.setId(id);
		
		User user = new User(100L, "Nam", "manam", "123456", "manam@email.com", "0123456789", "Admin");
		when(userService.getById(anyLong())).thenReturn(user);
		
		ResponseEntity<UserDeleteResponse> result = userController.delete(request);
		
		assertNotNull(result);
		assertEquals(200L, result.getBody().getCode());

		verify(userService,times(1)).getById(anyLong());
		verify(userService,times(1)).delete(anyLong());
		
	}
}
