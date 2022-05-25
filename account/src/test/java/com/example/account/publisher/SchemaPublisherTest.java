package com.example.account.publisher;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.common.constants.TestConstants;
import com.example.common.util.RabbitMQUtils;

@SpringBootTest
class SchemaPublisherTest {
	@Mock
	private RabbitMQUtils rabbitMQUtils;
	@InjectMocks
	private SchemaPublisher schemaPublisher = new SchemaPublisher(rabbitMQUtils);
	@Test
	void testCreateNewSchema() {
		assertDoesNotThrow(() -> schemaPublisher.createNewSchema(TestConstants.PASSED));
	}

}
