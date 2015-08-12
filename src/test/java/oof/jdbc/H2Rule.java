package oof.jdbc;

import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class H2Rule extends ExternalResource implements DataSource {

    private final H2DB db;

    public H2Rule() {
        this.db = new H2DB(this.toString());
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return super.apply(base, description);
    }

    @Override
    protected void before() throws Throwable {

    }

    @Override
    protected void after() {

    }

    @Override
    public Connection getConnection() throws SQLException {
        return db.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return db.getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return db.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        db.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        db.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return db.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return db.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return db.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return db.isWrapperFor(iface);
    }
}
