package com.man.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.man.UserService.enity.User;
import com.man.UserService.repository.UserRepository;
import com.man.UserService.request.UserCreateRequest;
import com.man.UserService.request.UserEditRequest;
import com.man.UserService.request.UserGetByIdRequest;
import com.man.UserService.request.UserGetByUsernameRequest;
import com.man.UserService.response.UserCreateResponse;
import com.man.UserService.response.UserEditResponse;
import com.man.UserService.response.UserGetByIdResponse;
import com.man.UserService.response.UserGetByUsernameResponse;
import com.man.UserService.service.impl.UserServiceImpl;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceTests {
	@Mock
	private UserRepository userRepository;

	private ModelMapper mapper;

	private PasswordEncoder encoder;

	@InjectMocks
	private UserServiceImpl impl;

	@BeforeEach
	void setUp() {
		mapper = new ModelMapper();
		encoder = new BCryptPasswordEncoder();
		impl = new UserServiceImpl(userRepository, mapper, encoder);
	}

	@Test
	void userListTest() {
		List<User> users = List.of(
				new User(100L, "Nam", "manam", encoder.encode("123456"), "manam@email.com", "0123456789", "Admin"),
				new User(101L, "Dong", "hadong", encoder.encode("123456"), "hadong@email.com", "0123476789", "Admin"),
				new User(102L, "Viet", "haviet", encoder.encode("123456"), "haviet@email.com", "0123476788", "User"));

		when(userRepository.findAll()).thenReturn(users);

		List<User> result = impl.userList();

		assertNotNull(result);
		assertEquals(3, result.size());
		assertTrue(encoder.matches("123456", result.get(1).getPassword()));

		verify(userRepository, times(1)).findAll();
	}

	@Test
	void createTest() {
		UserCreateRequest request = new UserCreateRequest("Nam", "manam", "123456", "manam@email.com", "0123456789",
				"Admin");

//		User user = new User(100L, "Nam", "manam", encoder.encode("123456"), "manam@email.com", "0123456789", "Admin");

		when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

		UserCreateResponse result = impl.create(request);
		assertNotNull(result);
		assertTrue(encoder.matches("123456", result.getPassword()));

		verify(userRepository, times(1)).save(any(User.class));

	}

	@Test
	void editTest() {
		UserEditRequest request = new UserEditRequest(100L, "Nam Mai", "123457", "manamtest@email.com", "0123456987",
				"User");

		User user = new User(100L, "Nam", "manam", encoder.encode("123456"), "manam@email.com", "0123456789", "Admin");

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

		UserEditResponse result = impl.edit(request);

		assertNotNull(result);
		assertEquals(user.getName(), result.getName());
		assertTrue(encoder.matches("123457", result.getPassword()));

		verify(userRepository, times(1)).save(any(User.class));
		verify(userRepository, times(1)).findById(anyLong());

	}

	@Test
	void deleteTest() {
		Long id = 100L;
		User user = new User(100L, "Nam", "manam", encoder.encode("123456"), "manam@email.com", "0123456789", "Admin");

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		impl.delete(id);

		verify(userRepository, times(1)).findById(anyLong());
		verify(userRepository, times(1)).delete(any(User.class));
	}

	@Test
	void getByIdTest() {
		Long id = 100L;
		User user = new User(100L, "Nam", "manam", encoder.encode("123456"), "manam@email.com", "0123456789", "Admin");

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		User result = impl.getById(id);

		assertNotNull(result);

		verify(userRepository, times(1)).findById(anyLong());
	}

	@Test
	void findByUsernameTest() {
		User user = new User(100L, "Nam", "manam", encoder.encode("123456"), "manam@email.com", "0123456789", "Admin");
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

		UserGetByUsernameRequest request = new UserGetByUsernameRequest("manam");

		UserGetByUsernameResponse result = impl.findByUsername(request);
		assertNotNull(result);
		assertEquals(user.getUsername(), result.getUsername());

		verify(userRepository, times(1)).findByUsername(anyString());

	}

	@Test
	void findByIdTest() {
		User user = new User(100L, "Nam", "manam", encoder.encode("123456"), "manam@email.com", "0123456789", "Admin");

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		UserGetByIdRequest request = new UserGetByIdRequest(100L);

		UserGetByIdResponse result = impl.findById(request);

		assertNotNull(result);

		verify(userRepository, times(1)).findById(anyLong());
	}

}
