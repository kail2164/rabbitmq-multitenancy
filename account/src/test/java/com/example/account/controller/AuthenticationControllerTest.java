package com.example.account.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.account.service.AuthenticationService;
import com.example.common.constants.TestConstants;
import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;
import com.example.common.dto.request.LoginRequest;
import com.example.common.dto.request.RegisterRequest;
import com.example.common.dto.response.LoginResponse;
import com.example.common.dto.response.RegisterResponse;
import com.example.common.util.JsonUtils;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private AuthenticationService authenticationService;
	
	@Test
	void testLogin_FAIL_NoContent() throws Exception {		
		String uri = "/api/auth/login";
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.error", containsString("request body is missing")))
				.andExpect(jsonPath("$.success", is(false)));
	}
	@Test
	void testLogin_FAIL_ExceptionThrew() throws Exception {
		doThrow(new CustomException(APIStatus.BAD_REQUEST, "Username cannot be null")).when(authenticationService)
				.login(any(LoginRequest.class));
		String uri = "/api/auth/login";
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(new LoginRequest())))
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.error", containsString("cannot be null")))
				.andExpect(jsonPath("$.success", is(false)));
	}

	@Test
	void testLogin_SUCCESS() throws Exception {
		LoginResponse result = new LoginResponse();
		result.setFirstName(TestConstants.PASSED);
		result.setLastName(TestConstants.PASSED);
		result.setRole("tester");
		result.setToken(TestConstants.PASSED);
		result.setUsername(TestConstants.PASSED);
		result.setId(1l);
		doReturn(result).when(authenticationService).login(any(LoginRequest.class));
		String uri = "/api/auth/login";
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(new LoginRequest())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result.id", is(1)))
				.andExpect(jsonPath("$.result.firstName", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.result.lastName", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.result.role", is("tester")))
				.andExpect(jsonPath("$.result.username", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.result.token", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.error", Matchers.nullValue()));
	}

	@Test
	void testLogout_FAIL_MissingToken() throws Exception {
		String uri = "/api/auth/logout";
		mvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.error", containsString("is not present")));
	}
	
	@Test
	void testLogout_FAIL_ExceptionThrew() throws Exception {
		doThrow(new CustomException(APIStatus.BAD_REQUEST, "Invalid token")).when(authenticationService)
				.logout(anyString());
		String uri = "/api/auth/logout";
		mvc.perform(get(uri).param("token", TestConstants.PASSED).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.error", containsString("Invalid")))
				.andExpect(jsonPath("$.success", is(false)));
	}
	

	@Test
	void testLogout_SUCCESS() throws Exception {
		String uri = "/api/auth/logout";
		mvc.perform(get(uri).param("token", TestConstants.PASSED).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result", containsString("success")))
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.error", Matchers.nullValue()));
	}

	
	@Test
	void testRegister_FAIL_NoContent() throws Exception {		
		String uri = "/api/auth/register";
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.error", containsString("request body is missing")))
				.andExpect(jsonPath("$.success", is(false)));
	}
	@Test
	void testRegister_FAIL_ExceptionThrew() throws Exception {
		doThrow(new CustomException(APIStatus.BAD_REQUEST, "Username cannot be null")).when(authenticationService)
				.register(any(RegisterRequest.class));
		String uri = "/api/auth/register";
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(new RegisterRequest())))
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.error", containsString("cannot be null")))
				.andExpect(jsonPath("$.success", is(false)));
	}

	@Test
	void testRegister_SUCCESS() throws Exception {
		RegisterResponse result = new RegisterResponse();
		result.setFirstName(TestConstants.PASSED);
		result.setLastName(TestConstants.PASSED);
		result.setRole("tester");
		result.setToken(TestConstants.PASSED);
		result.setUsername(TestConstants.PASSED);
		result.setId(1l);
		doReturn(result).when(authenticationService).register(any(RegisterRequest.class));
		String uri = "/api/auth/register";
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(new RegisterRequest())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result.id", is(1)))
				.andExpect(jsonPath("$.result.firstName", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.result.lastName", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.result.role", is("tester")))
				.andExpect(jsonPath("$.result.username", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.result.token", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.error", Matchers.nullValue()));
	}

}
