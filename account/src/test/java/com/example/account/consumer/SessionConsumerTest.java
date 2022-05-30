package com.example.account.consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.account.service.AuthenticationService;
import com.example.account.service.SessionService;
import com.example.common.constants.TestConstants;
import com.example.common.dto.CustomException;

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
	void testLogout_SUCCESS() {
		RemoteInvocationResult result = assertDoesNotThrow(() -> sessionConsumer.logout(TestConstants.PASSED));
		assertNotNull(result);
		Boolean bool = (Boolean) result.getValue();
		assertTrue(bool);
	}
	
	@Test
	void testGetUserDetails_FAIL_SessionServiceThrewException() {
		doThrow(UsernameNotFoundException.class).when(authenticationService).loadUserByUsername(anyString());
		assertThrows(UsernameNotFoundException.class, () -> sessionConsumer.getUserDetails(TestConstants.PASSED));
	}

	@Test
	void testGetUserDetails_SUCCESS() {
		UserDetails returnedUser = new User(TestConstants.PASSED, TestConstants.PASSED, new ArrayList<>());
		doReturn(returnedUser).when(authenticationService).loadUserByUsername(anyString());
		RemoteInvocationResult result = assertDoesNotThrow(() -> sessionConsumer.getUserDetails(TestConstants.PASSED));
		assertNotNull(result);
		UserDetails details = (UserDetails) result.getValue();
		assertNotNull(details);
	}	

}
