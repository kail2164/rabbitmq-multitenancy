package com.example.account.consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.remoting.support.RemoteInvocationResult;

import com.example.account.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountConsumerTest {
	@Mock
	private AccountRepository accountRepository;
	
	@InjectMocks
	private AccountConsumer accountConsumer = new AccountConsumer();
	
	@Test
	void testQueueFetchAllSchemas_FAIL_SQLExceptions() {
		doAnswer(invocation -> {throw new SQLException();}).when(accountRepository).getAllAccountId();
		assertThrows(SQLException.class, () -> accountConsumer.queueFetchAllSchemas());		
	}
	
	@Test
	void testQueueFetchAllSchemas_SUCCESS() {
		doReturn(new ArrayList<String>()).when(accountRepository).getAllAccountId();
		RemoteInvocationResult result = assertDoesNotThrow(() -> accountConsumer.queueFetchAllSchemas());
		List<String> listStr = (List<String>) result.getValue();
		assertNotNull(listStr);
		assertTrue(listStr.size() == 0);
		
		List<Long> returnedList = new ArrayList<>();
		returnedList.add(1l);
		doReturn(returnedList).when(accountRepository).getAllAccountId();
		result = assertDoesNotThrow(() -> accountConsumer.queueFetchAllSchemas());
		listStr = (List<String>) result.getValue();
		assertNotNull(listStr);
		assertTrue(listStr.size() > 0);
	}

}
