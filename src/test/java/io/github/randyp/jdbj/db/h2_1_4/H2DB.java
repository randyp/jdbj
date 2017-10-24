package io.github.randyp.jdbj.db.h2_1_4;

import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class H2DB implements DataSource, AutoCloseable {

    private final JdbcConnectionPool connectionPool;

    public H2DB(String name) {
        connectionPool = JdbcConnectionPool.create("jdbc:h2:mem:" + name + ";DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE", "sa", "sa");
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return connectionPool.getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return connectionPool.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        connectionPool.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        connectionPool.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return connectionPool.getLoginTimeout();
    }


    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return connectionPool.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return connectionPool.isWrapperFor(iface);
    }

    @Override
    public void close() throws Exception {
        connectionPool.dispose();
    }
}
