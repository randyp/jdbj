package com.github.randyp.jdbj.db.hsql_2_3;

import org.hsqldb.Server;
import org.junit.rules.ExternalResource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class HSqlRule extends ExternalResource implements DataSource {

    private Server server;

    public HSqlRule() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        checkDB();
        return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/jdbjdb", "sa", "");
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        checkDB();
        return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/jdbjdb", username, password);
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
        this.server = new Server();
        server.setLogWriter(null);
        server.setSilent(true);
        server.setDatabaseName(0, "jdbjdb");
        server.setDatabasePath(0, "file:jdbjdb");
        server.start();
    }

    @Override
    protected void after() {
        if (server != null) {
            Server server = this.server;
            this.server = null;
            try {
                server.stop();
            } catch (Exception e) {
                //ignore
            }
        }
    }

    private void checkDB() {
        if (server == null) {
            throw new IllegalStateException("trying to access db outside of evaluate... did you remember @ClassRule/@Rule?");
        }
    }
}
