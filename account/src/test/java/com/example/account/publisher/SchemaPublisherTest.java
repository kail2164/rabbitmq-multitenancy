package com.example.account.publisher;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.common.constants.TestConstants;

@SpringBootTest
class SchemaPublisherTest {
	private SchemaPublisher schemaPublisher = new SchemaPublisher();
	@Test
	void testCreateNewSchema() {
		assertDoesNotThrow(() -> schemaPublisher.createNewSchema(TestConstants.PASSED));
	}

}
