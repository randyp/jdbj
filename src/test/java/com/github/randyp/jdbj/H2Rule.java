package com.github.randyp.jdbj;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class H2Rule implements TestRule, DataSource {

    private H2DB db;

    public H2Rule() {
        this.db = new H2DB(this.toString());
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getDb().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getDb().getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return getDb().getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        getDb().setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        getDb().setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return getDb().getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return getDb().getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return getDb().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return getDb().isWrapperFor(iface);
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try (H2DB db = new H2DB(H2Rule.this.toString())) {
                    H2Rule.this.db = db;
                    statement.evaluate();
                }finally {
                    db = null;
                }
            }
        };
    }

    private DataSource getDb() {
        if(db == null){
            throw new IllegalStateException("trying to access db outside of evaluate... did you remember @ClassRule/@TestRule?");
        }
        return db;
    }
}