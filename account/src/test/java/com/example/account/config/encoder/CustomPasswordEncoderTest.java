package com.example.account.config.encoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.example.common.constants.TestConstants;
import com.example.common.dto.CustomException;

class CustomPasswordEncoderTest {

	@InjectMocks
	private CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();

	@Test
	void testEncode_FAIL_NullPassword() {
		//exception will return null
		String password = assertDoesNotThrow(() -> passwordEncoder.encode(null));
		assertNull(password);
	}
	
	
	
	@Test
	void testEncode_SUCCESS() {
		String password = assertDoesNotThrow(() -> passwordEncoder.encode(TestConstants.PASSED));
		assertTrue(password.length() > TestConstants.PASSED.length());
		assertTrue(password.contains(":"));
	}

	@Test
	void testCreateHashString_FAIL_NullString() {
		String nullValue = null;
		Exception ex = assertThrows(CustomException.class, () -> passwordEncoder.createHash(nullValue));
		String expectedMessage = "cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testCreateHashString_FAIL_EmptyString() {
		Exception ex = assertThrows(CustomException.class, () -> passwordEncoder.createHash(TestConstants.BLANK));
		String expectedMessage = "cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testCreateHashString_SUCCESS() {
		String hashed = assertDoesNotThrow(() -> passwordEncoder.createHash(TestConstants.PASSED));
		assertTrue(hashed.length() > TestConstants.PASSED.length());
		assertTrue(hashed.contains(":"));
	}
	
	@Test
	void testCreateHashCharArray_FAIL_NullString() {
		char[] empty = null;
 		Exception ex = assertThrows(CustomException.class, () -> passwordEncoder.createHash(empty));
		String expectedMessage = "cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	
	@Test
	void testCreateHashCharArray_SUCCESS() {
		String hashed = assertDoesNotThrow(() -> passwordEncoder.createHash(TestConstants.PASSED.toCharArray()));
		assertTrue(hashed.length() > TestConstants.PASSED.length());
		assertTrue(hashed.contains(":"));
	}
	
	@Test
	void testCreateHashCharArray() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testValidatePasswordStringString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testValidatePasswordCharArrayString() {
		fail("Not yet implemented"); // TODO
	}

}
