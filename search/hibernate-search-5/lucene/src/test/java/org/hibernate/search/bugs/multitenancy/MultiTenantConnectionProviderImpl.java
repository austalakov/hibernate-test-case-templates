package org.hibernate.search.bugs.multitenancy;

import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionCreator;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.ServiceRegistryImplementor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider {

    @Override
    public Connection getAnyConnection() throws SQLException {
        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
        ServiceRegistryImplementor serviceRegistry = (ServiceRegistryImplementor) registryBuilder.build();

        Map settings = serviceRegistry.getService(ConfigurationService.class).getSettings();

        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL(settings.get(Environment.URL).toString());
        ds.setUser(settings.get(Environment.USER).toString());

        return ds.getConnection();
    }
    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }
    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        final Connection connection = getAnyConnection();
        try {
            if (tenantIdentifier != null) {
                connection.createStatement().execute("SET SCHEMA " + tenantIdentifier + ";");
            } else {
                connection.createStatement().execute("SET SCHEMA PUBLIC;");
            }
        }
        catch ( SQLException e ) {
            throw new HibernateException(
                    "Problem setting schema to " + tenantIdentifier,
                    e
            );
        }
        return connection;
    }
    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        try {
            connection.createStatement().execute("SET SCHEMA PUBLIC;");
        }
        catch ( SQLException e ) {
            throw new HibernateException(
                    "Problem setting schema to " + tenantIdentifier,
                    e
            );
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