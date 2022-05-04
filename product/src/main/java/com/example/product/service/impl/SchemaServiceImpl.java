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

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaExport.Action;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.schema.TargetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.example.product.model.Product;
import com.example.product.publisher.AccountPublisher;
import com.example.product.service.SchemaService;

@Service
@DependsOn("rabbitMQUtil")
public class SchemaServiceImpl implements SchemaService, InitializingBean {
	@Autowired
	private DataSource dataSource;
	@Autowired
	AccountPublisher accountPublisher;
	@Autowired
	org.springframework.core.env.Environment env;
	@Autowired
	ExecutorService singleThreadExecutor;

	Logger logger = LoggerFactory.getLogger(SchemaServiceImpl.class);

	@Override
	public void createSchema(String schema) {
		Connection connection = null;
		Map<String, String> settings = new HashMap<>();
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			connection.createStatement().execute("CREATE SCHEMA " + schema + ";");
			settings.put(Environment.DEFAULT_SCHEMA, schema);
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(settings).build();
			hibernateConfigSchema(schema, serviceRegistry, true);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void afterPropertiesSet() {
		try {
			Map<String, String> settings = new HashMap<>();
			settings.put(Environment.DRIVER, "org.postgresql.Driver");
			settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
			settings.put(Environment.URL, env.getProperty("spring.datasource.url"));
			settings.put(Environment.USER, env.getProperty("spring.datasource.username"));
			settings.put(Environment.PASS, env.getProperty("spring.datasource.password"));
			List<String> schemas = accountPublisher.fetchAllSchemas();
			if (schemas == null) {
				return;
			}
			Runnable runnable = new Runnable() {
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
								Connection connection = dataSource.getConnection();
								connection.createStatement().execute("CREATE SCHEMA " + schema + ";");
								isCreate = true;
								connection.close();
							}
							settings.put(Environment.DEFAULT_SCHEMA, schema);
							ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(settings).build();
							hibernateConfigSchema(schema, serviceRegistry, isCreate);
							isCreate = false;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			};
			singleThreadExecutor.execute(runnable);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void hibernateConfigSchema(String schema, ServiceRegistry serviceRegistry, boolean isCreate) {
		MetadataSources metadata = new MetadataSources(serviceRegistry);
		metadata.addAnnotatedClass(Product.class);
		EnumSet<TargetType> enumSet = EnumSet.of(TargetType.DATABASE);
		if (isCreate) {
			SchemaExport schemaExport = new SchemaExport();
			schemaExport.execute(enumSet, Action.CREATE, metadata.buildMetadata());
		} else {
			SchemaUpdate schemaUpdate = new SchemaUpdate();
			schemaUpdate.execute(enumSet, metadata.buildMetadata());
		}
	}
}
