package com.example.product.multitenancy;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableJpaRepositories(basePackages = {
		"com.example.product.repository" }, entityManagerFactoryRef = "tenantEntityManagerFactory", transactionManagerRef = "tenantTransactionManager")
public class TenantPersistentConfig {
	@Autowired
	JpaProperties jpaProperties;

	@Autowired
	ConfigurableListableBeanFactory beanFactory;

	@Autowired
	protected org.springframework.core.env.Environment env;

	@Bean
	public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory(DataSource dataSource,
			MultiTenantConnectionProvider connectionProvider, CurrentTenantIdentifierResolver currentTenantIdentifier) {
		Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
		properties.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(beanFactory));
		properties.remove(AvailableSettings.DEFAULT_SCHEMA);
		properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
		properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
		properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifier);
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setPersistenceUnitName("tenant-persistence-unit");
		em.setDataSource(dataSource);
		em.setPackagesToScan("com.example.product.campaign.model");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaPropertyMap(properties);
		return em;
	}

	@Bean
	public JpaTransactionManager tenantTransactionManager(EntityManagerFactory tenantEntityManagerFactory) {
		JpaTransactionManager tenantTransactionManager = new JpaTransactionManager();
		tenantTransactionManager.setEntityManagerFactory(tenantEntityManagerFactory);
		return tenantTransactionManager;
	}
}
