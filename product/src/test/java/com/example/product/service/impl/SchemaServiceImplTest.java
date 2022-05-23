package com.example.product.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;

import javax.sql.DataSource;

import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import com.example.common.constants.TestConstants;
import com.example.product.publisher.AccountPublisher;
import com.example.product.service.SchemaService;

@SpringBootTest(classes = { SchemaServiceImpl.class })
@Import(SchemaServiceTestContextConfiguration.class)
class SchemaServiceImplTest {
	@MockBean
	private DataSource dataSource;
	@Mock
	private AccountPublisher accountPublisher;
	@Mock
	private Environment env;
	@Mock
	private ExecutorService singleThreadExecutor;
	@Mock
	private Connection connection;
	@Mock
	private SchemaExport schemaExport;
	@Mock
	private SchemaUpdate schemaUpdate;
	@InjectMocks
	private SchemaService schemaService = new SchemaServiceImpl();

	@Test
	void testCreateSchema() throws SQLException {		
		doReturn(Mockito.mock(Statement.class)).when(connection).createStatement();
		SchemaService spy = Mockito.spy(schemaService);
		spy.createSchema(TestConstants.PASSED);
	}

	@Test
	void testAfterPropertiesSet() throws Exception {
		schemaService.afterPropertiesSet();
	}

}
