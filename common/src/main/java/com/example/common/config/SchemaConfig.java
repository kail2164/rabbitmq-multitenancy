package com.example.common.config;

import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class SchemaConfig {
	@Bean
	public SchemaExport schemaExport() {
		return new SchemaExport();
	}
	@Bean
	public SchemaUpdate schemaUpdate() {
		return new SchemaUpdate();
	}
}
