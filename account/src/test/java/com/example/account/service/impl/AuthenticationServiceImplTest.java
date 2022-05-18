package com.example.account.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.account.model.Account;
import com.example.account.publisher.SchemaPublisher;
import com.example.account.repository.AccountRepository;
import com.example.account.service.AccountService;
import com.example.account.service.AuthenticationService;
import com.example.account.service.SessionService;
import com.example.common.constants.TestConstants;
import com.example.common.dto.CustomException;
import com.example.common.dto.request.LoginRequest;
import com.example.common.dto.request.RegisterRequest;
import com.example.common.dto.response.LoginResponse;
import com.example.common.dto.response.RegisterResponse;

@SpringBootTest
class AuthenticationServiceImplTest {

	@Mock
	private AccountService accountService;
	@Mock
	private SessionService sessionService;
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private SchemaPublisher schemaPublisher;

	@InjectMocks
	private AuthenticationService authenticationService = new AuthenticationServiceImpl();

	@Test
	void testLogin_FAIL_NullUsername() {
		LoginRequest request = new LoginRequest();
		Exception ex = assertThrows(CustomException.class, () -> authenticationService.login(request));
		String expectedMessage = "cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		request.setUsername(TestConstants.BLANK);
		ex = assertThrows(CustomException.class, () -> authenticationService.login(request));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	@WithAnonymousUser
	void testLogin_FAIL_WrongUsername() {
		LoginRequest request = new LoginRequest();
		request.setUsername(TestConstants.PASSED);
		request.setPassword(TestConstants.PASSED);
		Exception ex = assertThrows(CustomException.class, () -> authenticationService.login(request));
		String expectedMessage = "does not exist";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		doReturn(Optional.of(new Account())).when(accountRepository).findByUsernameIgnoreCase(any(String.class));
		doThrow(BadCredentialsException.class).when(authenticationManager)
				.authenticate(any(UsernamePasswordAuthenticationToken.class));
		ex = assertThrows(CustomException.class, () -> authenticationService.login(request));
		expectedMessage = "is not correct!";
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	@WithAnonymousUser
	void testLogin_FAIL_NullPassword() {
		LoginRequest request = new LoginRequest();
		request.setUsername(TestConstants.PASSED);
		request.setPassword(TestConstants.BLANK);
		Exception ex = assertThrows(CustomException.class, () -> authenticationService.login(request));
		String expectedMessage = "cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		request.setPassword(TestConstants.BLANK);
		ex = assertThrows(CustomException.class, () -> authenticationService.login(request));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	@WithAnonymousUser
	void testLogin_FAIL_WrongPassword() {
		doReturn(Optional.of(new Account())).when(accountRepository).findByUsernameIgnoreCase(any(String.class));
		doThrow(BadCredentialsException.class).when(authenticationManager)
				.authenticate(any(UsernamePasswordAuthenticationToken.class));
		LoginRequest request = new LoginRequest();
		request.setUsername(TestConstants.PASSED);
		request.setPassword(TestConstants.PASSED);
		Exception ex = assertThrows(CustomException.class, () -> authenticationService.login(request));
		String expectedMessage = "is not correct!";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	@WithAnonymousUser
	void testLogin_SUCCESS() {
		Account returnedAccount = new Account();
		returnedAccount.setRole(TestConstants.PASSED);
		returnedAccount.setUsername(TestConstants.PASSED);
		returnedAccount.setPassword(TestConstants.PASSED);
		doReturn(Optional.of(returnedAccount)).when(accountRepository).findByUsernameIgnoreCase(any(String.class));
		doReturn(returnedAccount).when(accountService).find(any(String.class));
		LoginRequest request = new LoginRequest();
		request.setUsername(TestConstants.PASSED);
		request.setPassword(TestConstants.PASSED);
		LoginResponse response = assertDoesNotThrow(() -> authenticationService.login(request));
		assertNotNull(response);
		assertEquals(request.getUsername(), response.getUsername());
		assertNotNull(response.getToken());
		assertTrue(response.getToken().length() > 10);
	}

	@Test
	@WithMockUser(authorities = { "ROLE_ADMIN" })
	void testLogin_FAIL_LoggedInSession() {
		Account returnedAccount = new Account();
		returnedAccount.setRole(TestConstants.PASSED);
		returnedAccount.setUsername(TestConstants.PASSED);
		returnedAccount.setPassword(TestConstants.PASSED);
		doReturn(Optional.of(returnedAccount)).when(accountRepository).findByUsernameIgnoreCase(any(String.class));
		LoginRequest request = new LoginRequest();
		request.setUsername(TestConstants.PASSED);
		request.setPassword(TestConstants.PASSED);
		Exception ex = assertThrows(CustomException.class, () -> authenticationService.login(request));
		String expectedMessage = "already authenticated";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testLogin_FAIL_InvalidSession() {
		Account returnedAccount = new Account();
		returnedAccount.setRole(TestConstants.PASSED);
		returnedAccount.setUsername(TestConstants.PASSED);
		returnedAccount.setPassword(TestConstants.PASSED);
		doReturn(Optional.of(returnedAccount)).when(accountRepository).findByUsernameIgnoreCase(any(String.class));
		LoginRequest request = new LoginRequest();
		request.setUsername(TestConstants.PASSED);
		request.setPassword(TestConstants.PASSED);
		Exception ex = assertThrows(CustomException.class, () -> authenticationService.login(request));
		String expectedMessage = "Invalid session";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testRegister_FAIL_CreateMethodThrowsException() throws CustomException {
		doThrow(CustomException.class).when(accountService).create(any(Account.class));
		RegisterRequest request = new RegisterRequest();
		assertThrows(CustomException.class, () -> authenticationService.register(request));
	}

	@Test
	void testRegister_SUCCESS() throws CustomException {
		Account returnedAccount = new Account();
		returnedAccount.setId(TestConstants.ID);
		returnedAccount.setRole(TestConstants.PASSED);
		returnedAccount.setUsername(TestConstants.PASSED);
		returnedAccount.setPassword(TestConstants.PASSED);
		doReturn(returnedAccount).when(accountService).find(any(String.class));
		doReturn(returnedAccount).when(accountService).create(any(Account.class));
		RegisterRequest request = new RegisterRequest();
		request.setUsername(TestConstants.PASSED);
		request.setPassword(TestConstants.PASSED);
		RegisterResponse response = assertDoesNotThrow(() -> authenticationService.register(request));
		assertNotNull(response);
		assertEquals(request.getUsername(), response.getUsername());
		assertNotNull(response.getToken());
		assertTrue(response.getToken().length() > 10);
	}

	@Test
	void testLogout_SUCCESS() {
		assertDoesNotThrow(() -> authenticationService.logout(TestConstants.PASSED));
	}

}
