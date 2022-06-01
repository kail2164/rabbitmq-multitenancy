package com.example.account.consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.account.service.AuthenticationService;
import com.example.account.service.SessionService;
import com.example.common.constants.TestConstants;
import com.example.common.dto.CustomException;
import com.example.common.dto.UserDTO;
import com.example.common.dto.request.LoginRequest;
import com.example.common.dto.response.LoginResponse;

@ExtendWith(MockitoExtension.class)
class SessionConsumerTest {
	@Mock
	private SessionService sessionService;
	@Mock
	private AuthenticationService authenticationService;
	@InjectMocks
	private SessionConsumer sessionConsumer = new SessionConsumer(sessionService, authenticationService);

	@Test
	void testValidateToken_FAIL_SessionServiceThrewException() throws CustomException {
		doThrow(CustomException.class).when(sessionService).validateToken(anyString());
		assertThrows(CustomException.class, () -> sessionConsumer.validateToken(TestConstants.PASSED));
	}

	@Test
	void testValidateToken_SUCCESS() {
		RemoteInvocationResult result = assertDoesNotThrow(() -> sessionConsumer.validateToken(TestConstants.PASSED));
		assertNotNull(result);
		Boolean bool = (Boolean) result.getValue();
		assertTrue(bool);
	}
	@Test
	void testLogin_SUCCESS() throws CustomException {
		LoginResponse response = new LoginResponse();
		doReturn(response).when(authenticationService).login(any(LoginRequest.class));
		RemoteInvocationResult result = assertDoesNotThrow(() -> sessionConsumer.login(new LoginRequest()));
		assertNotNull(result);
		LoginResponse bool = (LoginResponse) result.getValue();
		assertNotNull(bool);
	}

	@Test
	void testLogout_SUCCESS() {
		RemoteInvocationResult result = assertDoesNotThrow(() -> sessionConsumer.logout(TestConstants.PASSED));
		assertNotNull(result);
		Boolean bool = (Boolean) result.getValue();
		assertTrue(bool);
	}
	
	@Test
	void testGetUserDetails_FAIL_SessionServiceThrewException() throws CustomException {
		doThrow(UsernameNotFoundException.class).when(authenticationService).authenticate(anyString());
		assertThrows(UsernameNotFoundException.class, () -> sessionConsumer.getUserDetails(TestConstants.PASSED));
	}

	@Test
	void testGetUserDetails_SUCCESS() throws CustomException {
		UserDTO returnedUser = new UserDTO(TestConstants.PASSED, TestConstants.PASSED, TestConstants.PASSED);
		doReturn(returnedUser).when(authenticationService).authenticate(anyString());
		RemoteInvocationResult result = assertDoesNotThrow(() -> sessionConsumer.getUserDetails(TestConstants.PASSED));
		assertNotNull(result);
		UserDTO details = (UserDTO) result.getValue();
		assertNotNull(details);
	}	

}
