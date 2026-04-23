package com.studentsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentsystem.dto.request.UserCreate;
import com.studentsystem.dto.request.UserLoginRequest;
import com.studentsystem.dto.response.SuccessLogin;
import com.studentsystem.dto.response.SuccessUserCreated;
import com.studentsystem.enums.RoleEnum;
import com.studentsystem.service.interfaces.UserService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	@DisplayName("POST /api/v1/create returns created user payload")
	void createUserShouldReturnSuccessPayload() throws Exception {
		UserCreate request = new UserCreate("student@test.com", "password123", "Student One", RoleEnum.STUDENT);
		SuccessUserCreated response = new SuccessUserCreated("student@test.com", "Student One", RoleEnum.STUDENT);

		when(userService.createUser(any(UserCreate.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.email").value("student@test.com"))
			.andExpect(jsonPath("$.fullName").value("Student One"))
			.andExpect(jsonPath("$.role").value("STUDENT"));

		verify(userService, times(1)).createUser(any(UserCreate.class));
	}

	@Test
	@DisplayName("POST /api/v1/passport/token returns access token payload")
	void getAccessTokenShouldReturnSuccessPayload() throws Exception {
		UserLoginRequest request = new UserLoginRequest("student@test.com", "password123");
		SuccessLogin response = new SuccessLogin("jwt-token", 900);

		when(userService.getAccessToken(any(UserLoginRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/passport/token")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").value("jwt-token"))
			.andExpect(jsonPath("$.expiresIn").value(900));

		verify(userService, times(1)).getAccessToken(any(UserLoginRequest.class));
	}

	@Test
	@DisplayName("POST /api/v1/passport/token propagates exception when service throws")
	void getAccessTokenShouldPropagateExceptionWhenServiceThrows() {
		UserLoginRequest request = new UserLoginRequest("student@test.com", "wrong-password");
		when(userService.getAccessToken(any(UserLoginRequest.class)))
			.thenThrow(new RuntimeException("Username or Password incorrect"));

		ServletException exception = Assertions.assertThrows(ServletException.class, () ->
			mockMvc.perform(post("/api/v1/passport/token")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andReturn()
		);

		Assertions.assertNotNull(exception.getCause());
		Assertions.assertEquals("Username or Password incorrect", exception.getCause().getMessage());

		verify(userService, times(1)).getAccessToken(any(UserLoginRequest.class));
	}
}
