package com.example.account.config.encoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.common.config.encoder.CustomPasswordEncoder;
import com.example.common.constants.TestConstants;
import com.example.common.dto.CustomException;

class CustomPasswordEncoderTest {
	private CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();

	@Test
	void testEncode_FAIL_NullPassword() {
		// exception will return null
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
	void testCreateHashString_FAIL_NullOrEmptyString() {
		String nullValue = null;
		Exception ex = assertThrows(CustomException.class, () -> passwordEncoder.createHash(nullValue));
		String expectedMessage = "cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		ex = assertThrows(CustomException.class, () -> passwordEncoder.createHash(TestConstants.BLANK));
		actualMessage = ex.getMessage();
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
	void testValidatePassword_FAIL_NullPassword() {
		String pwd = null;
		Exception ex = assertThrows(CustomException.class,
				() -> passwordEncoder.validatePassword(pwd, TestConstants.PASSED));
		String expectedMessage = "cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		ex = assertThrows(CustomException.class,
				() -> passwordEncoder.validatePassword(TestConstants.BLANK, TestConstants.PASSED));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testValidatePassword_FAIL_NullCorrectHash() {
		String hash = null;
		Exception ex = assertThrows(CustomException.class,
				() -> passwordEncoder.validatePassword(TestConstants.PASSED, hash));
		String expectedMessage = "cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		ex = assertThrows(CustomException.class,
				() -> passwordEncoder.validatePassword(TestConstants.PASSED, TestConstants.BLANK));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testValidatePassword_FAIL_BothNullOrEmpty() {
		String hash = null;
		String pwd = null;
		Exception ex = assertThrows(CustomException.class, () -> passwordEncoder.validatePassword(pwd, hash));
		String expectedMessage = "cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		ex = assertThrows(CustomException.class,
				() -> passwordEncoder.validatePassword(TestConstants.BLANK, TestConstants.BLANK));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testValidatePassword_FAIL_TextFormatForHash() {
		String hash = "ABCD";
		Exception ex = assertThrows(CustomException.class,
				() -> passwordEncoder.validatePassword(TestConstants.PASSED, hash));
		String expectedMessage = "input string";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testValidatePassword_FAIL_InvalidHashOutOfBounds() {
		String hash = "123456";
		String pwd = "edf";
		Exception ex = assertThrows(CustomException.class, () -> passwordEncoder.validatePassword(pwd, hash));
		String expectedMessage = "out of bounds";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		String hash2 = "123456:123456";
		ex = assertThrows(CustomException.class, () -> passwordEncoder.validatePassword(pwd, hash2));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testValidatePassword_FAIL_InvalidHash() {
		String hash = "123456:123456:123456";
		String pwd = "edf";
		boolean result = assertDoesNotThrow(() -> passwordEncoder.validatePassword(pwd, hash));		
		assertFalse(result);
	}

	@Test
	void testValidatePassword_SUCCESS() {
		assertTrue(assertDoesNotThrow(
				() -> passwordEncoder.validatePassword(TestConstants.VALID_PWD, TestConstants.VALID_HASH)));
	}

	@Test
	void testMatches_FAIL_NullOrEmptyPassword() {
		String pwd = TestConstants.BLANK;
		String hash = TestConstants.VALID_HASH;
		boolean result = assertDoesNotThrow(() -> passwordEncoder.matches(pwd, hash));
		assertFalse(result);
		
		result = assertDoesNotThrow(() -> passwordEncoder.matches(null, hash));
		assertFalse(result);
	}
	
	@Test
	void testMatches_FAIL_NullOrEmptyHash() {
		String pwd = TestConstants.PASSED;
		String hash = TestConstants.BLANK;
		boolean result = assertDoesNotThrow(() -> passwordEncoder.matches(pwd, hash));
		assertFalse(result);
		
		result = assertDoesNotThrow(() -> passwordEncoder.matches(pwd, null));
		assertFalse(result);
	}
	
	@Test
	void testMatches_FAIL_InvalidPwdOrHash() {
		String pwd = TestConstants.VALID_PWD;
		String hash = TestConstants.PASSED;
		boolean result = assertDoesNotThrow(() -> passwordEncoder.matches(pwd, hash));
		assertFalse(result);
		
		result = assertDoesNotThrow(() -> passwordEncoder.matches(TestConstants.PASSED, TestConstants.VALID_HASH));
		assertFalse(result);
	}
	
	@Test
	void testMatches_SUCCESS() {
		String pwd = TestConstants.VALID_PWD;
		String hash = TestConstants.VALID_HASH;
		boolean result = assertDoesNotThrow(() -> passwordEncoder.matches(pwd, hash));
		assertTrue(result);		
	}

}
