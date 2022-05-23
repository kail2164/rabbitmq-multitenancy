package com.example.product.service.impl;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;

import javax.sql.DataSource;

import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.example.common.util.RabbitMQUtils;
import com.example.product.config.ExecutorServiceConfig;
import com.example.product.publisher.AccountPublisher;

@TestConfiguration
public class SchemaServiceTestContextConfiguration {
	@Bean
	public RabbitMQUtils rabbitMQUtils() {
		return new RabbitMQUtils(Mockito.mock(RabbitTemplate.class));
	}
	@Bean
	public AccountPublisher accountPublisher() {
		return new AccountPublisher();
	}
	@Bean
	public ExecutorService singleThreadExecutor() {
		return new ExecutorServiceConfig().singleThreadExecutor();
	}
	@Bean
	public DataSource dataSource() {
		return Mockito.mock(DataSource.class);
	}
	@Bean
	public Connection connection() {
		return Mockito.mock(Connection.class);
	}	
	@Bean
	public SchemaExport schemaExport() {
		return Mockito.mock(SchemaExport.class);
	}
	@Bean
	public SchemaUpdate schemaUpdate() {
		return Mockito.mock(SchemaUpdate.class);
	}
}
