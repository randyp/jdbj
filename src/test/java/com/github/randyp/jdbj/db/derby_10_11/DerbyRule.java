package com.github.randyp.jdbj.db.derby_10_11;

import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.*;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

public class DerbyRule extends ExternalResource implements DataSource {

    private final String url = "jdbc:derby:jdbj;create=true";
    private final Properties defaultProperties = new Properties();

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
        after();
        try(Connection connection = getConnection()){
            /*
            try (PreparedStatement ps = connection.prepareStatement("CREATE SCHEMA information_schema")) {
                ps.execute();
            }
            */
            try (PreparedStatement ps = connection.prepareStatement("CREATE TABLE information_schema.tables(table_schema VARCHAR(500), table_name VARCHAR(500))")) {
                ps.execute();
            }
            try(PreparedStatement ps = connection.prepareStatement("INSERT INTO information_schema.tables(table_schema, table_name) VALUES ('information_schema', 'tables'), ('information_schema', 'schemata')")){
                ps.execute();
            }
        }
    }

    @Override
    protected void after() {
        try {
            try(Connection connection = getConnection()){
                try {
                    try (PreparedStatement ps = connection.prepareStatement("DROP TABLE information_schema.tables")) {
                        ps.execute();
                    }
                } catch (SQLException e) {
                    //ignore
                }
                /*
                try {
                    try (PreparedStatement ps = connection.prepareStatement("DROP SCHEMA information_schema")) {
                        ps.execute();
                    }
                } catch (SQLException e) {
                    //ignore
                }
                */
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
