package com.example.product.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.sql.DataSource;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaExport.Action;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.example.product.model.Product;
import com.example.product.publisher.AccountPublisher;
import com.example.product.service.SchemaService;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@DependsOn("rabbitMQUtils")
@Slf4j
@NoArgsConstructor
public class SchemaServiceImpl implements SchemaService {
	private DataSource dataSource;
	private AccountPublisher accountPublisher;
	private org.springframework.core.env.Environment env;
	private ExecutorService singleThreadExecutor;
	private Map<String, String> settings = new HashMap<>();
	private Connection connection;
	private SchemaExport schemaExport;
	private SchemaUpdate schemaUpdate;

	@Autowired
	public SchemaServiceImpl(DataSource dataSource, AccountPublisher accountPublisher,
			org.springframework.core.env.Environment env, ExecutorService singleThreadExecutor) {
		super();
		this.dataSource = dataSource;
		this.accountPublisher = accountPublisher;
		this.env = env;
		this.singleThreadExecutor = singleThreadExecutor;
		this.schemaExport = new SchemaExport();
		this.schemaUpdate = new SchemaUpdate();
		try {
			this.connection = dataSource.getConnection();
		} catch (SQLException e) {
			log.error("Error in createSchema: ", e);
		}

	}

	@Override
	public void createSchema(String schema) {
		try {
			connection.createStatement().execute("CREATE SCHEMA " + schema + ";");
			settings.put(Environment.DEFAULT_SCHEMA, schema);
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(settings).build();
			hibernateConfigSchema(schema, serviceRegistry, true);
			connection.close();
		} catch (SQLException e) {
			log.error("Error in createSchema: ", e);
		}
	}

	@Override
	public void afterPropertiesSet() {
		try {
			settings.put(Environment.DRIVER, "org.postgresql.Driver");
			settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
			settings.put(Environment.URL, env.getProperty("spring.datasource.url"));
			settings.put(Environment.USER, env.getProperty("spring.datasource.username"));
			settings.put(Environment.PASS, env.getProperty("spring.datasource.password"));			
			List<String> schemas = accountPublisher.fetchAllSchemas();
			if (schemas == null) {
				return;
			}
			singleThreadExecutor.execute(createSchema(schemas));
		} catch (Throwable e) {
			log.error("Error in afterPropertiesSet: ", e);
		}
	}

	private Runnable createSchema(List<String> schemas) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					List<String> existingSchema = new ArrayList<>();
					DatabaseMetaData md = dataSource.getConnection().getMetaData();
					ResultSet rs = md.getSchemas();
					while (rs.next()) {
						existingSchema.add(rs.getString(1));
					}
					boolean isCreate = false;
					for (String schema : schemas) {
						if (!existingSchema.contains(schema)) {
							connection.createStatement().execute("CREATE SCHEMA " + schema + ";");
							isCreate = true;
							connection.close();
						}
						settings.put(Environment.DEFAULT_SCHEMA, schema);
						ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(settings)
								.build();
						hibernateConfigSchema(schema, serviceRegistry, isCreate);
						isCreate = false;
					}
				} catch (SQLException e) {
					log.error("Error in afterPropertiesSet: ", e);
				}
			}
		};
	}

	private void hibernateConfigSchema(String schema, ServiceRegistry serviceRegistry, boolean isCreate) {
		MetadataSources metadataSources = new MetadataSources(serviceRegistry);
		metadataSources.addAnnotatedClass(Product.class);
		EnumSet<TargetType> enumSet = EnumSet.of(TargetType.DATABASE);
		Metadata metadata =  metadataSources.buildMetadata();
		if (isCreate) {
			schemaExport.execute(enumSet, Action.CREATE, metadata);
		} else {
			schemaUpdate.execute(enumSet, metadata);
		}
	}
}
