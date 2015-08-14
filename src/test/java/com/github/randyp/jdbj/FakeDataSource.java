package com.github.randyp.jdbj;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class FakeDataSource<E extends Connection> implements DataSource {


    private final Supplier<E> supplier;

    public FakeDataSource(Supplier<E> supplier) {
        this.supplier = supplier;
    }

    @Override
    public E getConnection() throws SQLException {
        return supplier.get();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new UnsupportedOperationException();
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

}
