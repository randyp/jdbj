package io.codemonastery.jdbj.jdbj.db.postgres_9_4;

import org.junit.rules.ExternalResource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class PGRule extends ExternalResource implements DataSource {

    private final String url = "jdbc:postgresql://localhost:5432/jdbj";
    private final Properties defaultProperties = new Properties();
    private boolean open = false;

    public PGRule() {
        defaultProperties.setProperty("user", "jdbj");
        defaultProperties.setProperty("password", "jdbj");
    }

    @Override
    public Connection getConnection() throws SQLException {
        checkOpen();
        return DriverManager.getConnection(url, defaultProperties);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        checkOpen();
        final Properties properties = new Properties(defaultProperties);
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        return DriverManager.getConnection(url, properties);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void before() throws Throwable {
        open = true;
    }

    @Override
    protected void after() {
        open = false;
    }

    private void checkOpen() {
        if(!open){
            throw new IllegalStateException("Accessing db outsite of tests");
        }
    }
}

