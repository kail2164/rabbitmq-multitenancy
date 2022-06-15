package com.example.account.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.account.model.UserSession;
import com.example.account.repository.UserSessionRepository;
import com.example.account.service.SessionService;
import com.example.common.constants.TestConstants;
import com.example.common.dto.CustomException;

@ExtendWith(MockitoExtension.class)
class SessionServiceImplTest {
	
	@Mock
	private UserSessionRepository userSessionRepository;
	
	@InjectMocks
	private SessionService sessionService = new SessionServiceImpl();

	@Test
	void testSetSession_FAIL_NullUserSession() {
		Exception ex = assertThrows(CustomException.class, () -> sessionService.setSession(null, null));
		String expectedMessage = "cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testSetSession_FAIL_NullToken() {
		Exception ex = assertThrows(CustomException.class, () -> sessionService.setSession(null, new UserSession()));
		String expectedMessage = "cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testSetSession_SUCCESS() {
		assertDoesNotThrow(() -> sessionService.setSession(TestConstants.PASSED, new UserSession()));
	}

	@Test
	void testGetSession_SUCCESS_NullSessionRetrieved() {
		UserSession session = assertDoesNotThrow(() -> sessionService.getSession(TestConstants.BLANK));
		assertNull(session);		
	}
	
	@Test
	void testGetSession_SUCCESS_SessionRetrieved() {		
		assertDoesNotThrow(() -> sessionService.setSession(TestConstants.PASSED, new UserSession()));
		UserSession session = assertDoesNotThrow(() -> sessionService.getSession(TestConstants.PASSED));
		assertNotNull(session);
	}
	
	@Test
	void testRemoveOldTokens_FAIL_NullSession() {
		Exception ex = assertThrows(CustomException.class, () -> sessionService.removeOldTokens(null));
		String expectedMessage = "cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));	}

	@Test
	void testRemoveOldTokens_SUCCESS_FoundSession() {
		UserSession session = new UserSession();
		assertDoesNotThrow(() -> sessionService.setSession(TestConstants.PASSED, session));
		assertDoesNotThrow(() -> sessionService.removeOldTokens(session));
	}
	
	@Test
	void testRemoveOldTokens_SUCCESS_NotFoundSession() {
		UserSession session = new UserSession();
		session.setToken(TestConstants.PASSED);
		assertDoesNotThrow(() -> sessionService.removeOldTokens(session));
	}
	
	@Test
	void testRemoveToken_FAIL_NullSession() {
		Exception ex = assertThrows(CustomException.class, () -> sessionService.removeTokens(null));
		String expectedMessage = "cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testRemoveToken_SUCCESS_FoundSession() {
		assertDoesNotThrow(() -> sessionService.removeTokens(List.of(TestConstants.PASSED)));
	}
	
	@Test
	void testRemoveToken_SUCCESS_NotFoundSession() {
		assertDoesNotThrow(() -> sessionService.removeTokens(List.of(TestConstants.PASSED)));
	}
	
	@Test
	void testIsTokenExists_SUCCESS_True() {
		boolean result = assertDoesNotThrow(() -> sessionService.isTokenExists(TestConstants.PASSED));
		assertTrue(result);
	}
	
	@Test
	void testIsTokenExists_SUCCESS_False() {
		boolean result = assertDoesNotThrow(() -> sessionService.isTokenExists(TestConstants.INVALID_MAX_LENGTH_STR));
		assertFalse(result);
	}
	
	@Test
	void testValidateToken_SUCCESS_NotFoundTokenAnywhere() {
		Exception ex = assertThrows(CustomException.class, () -> sessionService.validateToken(TestConstants.INVALID_MAX_LENGTH_STR));
		String expectedMessage = "Invalid token";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testValidateToken_SUCCESS_FoundTokenInHashMap() {
		assertDoesNotThrow(() -> sessionService.validateToken(TestConstants.PASSED));
	}
	
	@Test
	void testValidateToken_SUCCESS_FoundTokenInDB() {
		doReturn(Optional.of(new UserSession())).when(userSessionRepository).findById(anyString());
		assertDoesNotThrow(() -> sessionService.validateToken(TestConstants.BLANK));
	}
	
	

}
