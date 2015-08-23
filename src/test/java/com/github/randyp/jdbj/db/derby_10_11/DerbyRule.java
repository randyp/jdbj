package com.github.randyp.jdbj.db.derby_10_11;

import org.junit.rules.ExternalResource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class DerbyRule extends ExternalResource implements DataSource {

    private final String url = "jdbc:derby:jdbjdb;create=true";
    private final Properties defaultProperties = new Properties();

    private boolean open;

    public DerbyRule() {
        defaultProperties.setProperty("user", "jdbj");
        defaultProperties.setProperty("password", "jdbj");

        try {
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, defaultProperties);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
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
