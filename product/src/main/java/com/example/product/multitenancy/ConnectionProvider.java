package com.example.product.multitenancy;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

@Component
public class ConnectionProvider implements MultiTenantConnectionProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1532155100098995590L;
	private DataSource dataSource;

	@Autowired
	public ConnectionProvider(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Connection getAnyConnection() throws SQLException {
		return dataSource.getConnection();
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		connection.close();
	}

	@Override
	public Connection getConnection(String tenantIdentifier) throws SQLException {
		final Connection connection = getAnyConnection();
		try {
			connection.createStatement().execute("SET search_path TO " + tenantIdentifier + ";");
		} catch (SQLException e) {
			throw new HibernateException(
					"Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e);
		}
		return connection;
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		try {
			connection.createStatement().execute("SET search_path TO public;");
		} catch (SQLException e) {
			throw new HibernateException(
					"Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e);
		}
		connection.close();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> unwrapType) {
		return null;
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return true;
	}

}
