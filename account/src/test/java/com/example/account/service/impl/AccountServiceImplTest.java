package com.example.account.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.account.model.Account;
import com.example.account.repository.AccountRepository;
import com.example.account.service.AccountService;
import com.example.common.constants.TestConstants;
import com.example.common.dto.CustomException;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
	
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private AccountService accountService = new AccountServiceImpl();

	@Test
	void testCreate_FAIL_NullUsername() {
		Account account = new Account();
		Exception ex = assertThrows(CustomException.class, () -> accountService.create(account));
		String expectedMessage = "Username cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));

		account.setUsername(TestConstants.BLANK);
		ex = assertThrows(CustomException.class, () -> accountService.create(account));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
	}

	@Test
	void testCreate_FAIL_NullRole() {
		Account account = new Account();
		account.setUsername(TestConstants.PASSED);
		Exception ex = assertThrows(CustomException.class, () -> accountService.create(account));
		String expectedMessage = "Role cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));

		account.setRole(TestConstants.BLANK);
		ex = assertThrows(CustomException.class, () -> accountService.create(account));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
	}

	@Test
	void testCreate_FAIL_InvalidUsernameLength() {
		Account account = new Account();
		account.setUsername(TestConstants.INVALID_MIN_LENGTH_STR);
		account.setRole(TestConstants.PASSED);
		Exception ex = assertThrows(CustomException.class, () -> accountService.create(account));
		String expectedMessage = "Username's length is from 2-255 characters";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
	}

	@Test
	void testCreate_FAIL_InvalidFieldLength() {
		Account account = new Account();
		account.setUsername(TestConstants.PASSED);
		account.setRole(TestConstants.PASSED);
		account.setFirstName(TestConstants.INVALID_MAX_LENGTH_STR);
		Exception ex = assertThrows(CustomException.class, () -> accountService.create(account));
		String expectedMessage = "max length is 255 characters";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		account.setFirstName(TestConstants.PASSED);
		account.setLastName(TestConstants.INVALID_MAX_LENGTH_STR);
		ex = assertThrows(CustomException.class, () -> accountService.create(account));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		account.setFirstName(TestConstants.PASSED);
		account.setLastName(TestConstants.PASSED);
		account.setRole(TestConstants.INVALID_MAX_LENGTH_STR);
		ex = assertThrows(CustomException.class, () -> accountService.create(account));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testCreate_FAIL_InvalidPassword() {
		Account account = new Account();
		account.setUsername(TestConstants.PASSED);
		account.setRole(TestConstants.PASSED);
		account.setFirstName(TestConstants.PASSED);
		account.setLastName(TestConstants.PASSED);
		Exception ex = assertThrows(CustomException.class, () -> accountService.create(account));
		String expectedMessage = "8 characters";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		account.setPassword(TestConstants.INVALID_MIN_LENGTH_STR);
		ex = assertThrows(CustomException.class, () -> accountService.create(account));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testCreate_FAIL_UnexpectedError() {
		doThrow(new IllegalArgumentException("Error")).when(accountRepository).save(any(Account.class));
		Account account = new Account();
		account.setUsername(TestConstants.PASSED);
		account.setRole(TestConstants.PASSED);
		account.setFirstName(TestConstants.PASSED);
		account.setLastName(TestConstants.PASSED);
		account.setPassword(TestConstants.PASSED);

		assertThrows(CustomException.class, () -> accountService.create(account));
	}

	@Test
	void testCreate_SUCCESS() {
		Account returnedAccount = new Account();
		returnedAccount.setId(TestConstants.ID);
		when(accountRepository.save(any(Account.class))).thenReturn(returnedAccount);

		Account account = new Account();
		account.setUsername(TestConstants.PASSED);
		account.setRole(TestConstants.PASSED);
		account.setFirstName(TestConstants.PASSED);
		account.setLastName(TestConstants.PASSED);
		account.setPassword(TestConstants.PASSED);

		Account saved = assertDoesNotThrow(() -> accountService.create(account));
		assertNotNull(saved);
		assertNotNull(saved.getId());
		assertEquals(TestConstants.ID, saved.getId());
	}

	@Test
	void testUpdate_FAIL_NullUsername() {
		Account account = new Account();
		Exception ex = assertThrows(CustomException.class,
				() -> accountService.update(TestConstants.ID, account, TestConstants.NULLABLE_UPDATE_TRUE));
		String expectedMessage = "Username cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));

		account.setUsername(TestConstants.BLANK);
		ex = assertThrows(CustomException.class, () -> accountService.update(TestConstants.ID, account, TestConstants.NULLABLE_UPDATE_TRUE));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
	}

	@Test
	void testUpdate_FAIL_NullRole() {
		Account account = new Account();
		account.setUsername(TestConstants.PASSED);
		Exception ex = assertThrows(CustomException.class,
				() -> accountService.update(TestConstants.ID, account, TestConstants.NULLABLE_UPDATE_TRUE));
		String expectedMessage = "Role cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));

		account.setRole(TestConstants.BLANK);
		ex = assertThrows(CustomException.class, () -> accountService.update(TestConstants.ID, account, TestConstants.NULLABLE_UPDATE_TRUE));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
	}

	@Test
	void testUpdate_InvalidUsernameLength() {
		Account account = new Account();
		account.setUsername(TestConstants.INVALID_MIN_LENGTH_STR);
		account.setRole(TestConstants.PASSED);
		Exception ex = assertThrows(CustomException.class,
				() -> accountService.update(TestConstants.ID, account, TestConstants.NULLABLE_UPDATE_TRUE));
		String expectedMessage = "Username's length is from 2-255 characters";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
	}

	@Test
	void testUpdate_InvalidFieldLength() {
		Account account = new Account();
		account.setUsername(TestConstants.PASSED);
		account.setRole(TestConstants.PASSED);
		account.setFirstName(TestConstants.INVALID_MAX_LENGTH_STR);
		Exception ex = assertThrows(CustomException.class,
				() -> accountService.update(TestConstants.ID, account, TestConstants.NULLABLE_UPDATE_TRUE));
		String expectedMessage = "max length is 255 characters";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		account.setFirstName(TestConstants.PASSED);
		account.setLastName(TestConstants.INVALID_MAX_LENGTH_STR);
		ex = assertThrows(CustomException.class, () -> accountService.update(TestConstants.ID, account, TestConstants.NULLABLE_UPDATE_TRUE));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		account.setFirstName(TestConstants.PASSED);
		account.setLastName(TestConstants.PASSED);
		account.setRole(TestConstants.INVALID_MAX_LENGTH_STR);
		ex = assertThrows(CustomException.class, () -> accountService.update(TestConstants.ID, account, TestConstants.NULLABLE_UPDATE_TRUE));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate_FAIL_NotFoundById() {
		doReturn(Optional.empty()).when(accountRepository).findById(any(Long.class));
		Account account = new Account();
		account.setUsername(TestConstants.PASSED);
		account.setRole(TestConstants.PASSED);
		account.setFirstName(TestConstants.PASSED);
		account.setLastName(TestConstants.PASSED);
		account.setPassword(TestConstants.PASSED);
		Exception ex = assertThrows(CustomException.class,
				() -> accountService.update(TestConstants.ID, account, TestConstants.NULLABLE_UPDATE_TRUE));
		String expectedMessage = "Not found";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate_SUCCESS() {
		Account returnedAccount = new Account();
		returnedAccount.setId(TestConstants.ID);
		returnedAccount.setFirstName(TestConstants.BLANK);
		returnedAccount.setLastName(TestConstants.BLANK);
		doReturn(Optional.of(returnedAccount)).when(accountRepository).findById(any(Long.class));
		doReturn(returnedAccount).when(accountRepository).save(any(Account.class));
		Account account = new Account();
		account.setUsername(TestConstants.PASSED);
		account.setRole(TestConstants.PASSED);
		account.setFirstName(TestConstants.PASSED);
		account.setLastName(TestConstants.PASSED);
		account.setPassword(TestConstants.PASSED);

		Account saved = assertDoesNotThrow(() -> accountService.update(TestConstants.ID, account, TestConstants.NULLABLE_UPDATE_TRUE));
		assertNotNull(saved);
		assertNotNull(saved.getId());
		assertEquals(TestConstants.ID, saved.getId());
		assertEquals(TestConstants.PASSED, saved.getFirstName());
		assertEquals(TestConstants.PASSED, saved.getLastName());
		assertNotEquals(saved, account);
	}

	@Test
	void testDelete_FAIL_NotFoundById() {
		doReturn(Optional.empty()).when(accountRepository).findById(any(Long.class));

		Exception ex = assertThrows(CustomException.class, () -> accountService.delete(TestConstants.ID));
		String expectedMessage = "Not found";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDelete_SUCCESS() {
		doReturn(Optional.of(new Account())).when(accountRepository).findById(any(Long.class));
		assertDoesNotThrow(() -> accountService.delete(TestConstants.ID));
		verify(accountRepository).delete(any(Account.class));
	}

	@Test
	void testDelete_FAIL_UnexpectedError() {
		assertThrows(CustomException.class, () -> accountService.delete(TestConstants.ID));
	}
	
	@Test
	void testFind_FAIL_NotFound() {
		doReturn(Optional.empty()).when(accountRepository).findByUsernameIgnoreCase(any(String.class));
		assertThrows(UsernameNotFoundException.class, () -> accountService.find(TestConstants.BLANK));
	}
	
	@Test
	void testFind_SUCCESS_NotFound() {
		Account returnedAccount = new Account();
		returnedAccount.setId(TestConstants.ID);
		returnedAccount.setFirstName(TestConstants.PASSED);
		returnedAccount.setLastName(TestConstants.PASSED);
		doReturn(Optional.of(returnedAccount)).when(accountRepository).findByUsernameIgnoreCase(any(String.class));
		Account account = assertDoesNotThrow(() -> accountService.find(TestConstants.BLANK));
		assertNotNull(account);
		assertEquals(TestConstants.ID, account.getId());
		assertEquals(TestConstants.PASSED, account.getFirstName());
		assertEquals(TestConstants.PASSED, account.getLastName());
	}

}
