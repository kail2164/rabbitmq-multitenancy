package com.example.product.service.impl;

import static org.mockito.Mockito.doReturn;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.sql.DataSource;

import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.example.common.constants.TestConstants;
import com.example.product.publisher.AccountPublisher;
import com.example.product.service.SchemaService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SchemaServiceImplTest {
	@Mock
	private DataSource dataSource;
	@Mock
	private AccountPublisher accountPublisher;
	@Mock
	private Environment env;
	@Mock
	private ExecutorService singleThreadExecutor;
	@Mock
	private SchemaExport schemaExport;
	@Mock
	private SchemaUpdate schemaUpdate;
	@InjectMocks
	private SchemaService schemaService = new SchemaServiceImpl();
	@Mock
	Connection connection;	

	@BeforeAll
	void setup() throws Exception {
		doReturn("jdbc:postgresql://localhost:5432/db_product").when(env).getProperty("spring.datasource.url");
		doReturn("ad").when(env).getProperty("spring.datasource.username");
		doReturn("12345678!Ad").when(env).getProperty("spring.datasource.password");
		doReturn(connection).when(dataSource).getConnection();
		List<String> listSchemas = new ArrayList<>();
		listSchemas.add(TestConstants.PASSED);
		doReturn(listSchemas).when(accountPublisher).fetchAllSchemas();
	}
	
	@Test
	@Order(1)
	void testAfterPropertiesSet() throws Exception {
		
		schemaService.afterPropertiesSet();
	}

	@Test
	@Order(2)
	void testCreateSchema() throws SQLException {	
		doReturn(Mockito.mock(Statement.class)).when(connection).createStatement();
		schemaService.createSchema(TestConstants.PASSED + "1");
	}

}
