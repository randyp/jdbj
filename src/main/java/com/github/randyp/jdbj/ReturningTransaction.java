package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ConnectionCallable;
import com.github.randyp.jdbj.lambda.ConnectionSupplier;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ReturningTransaction<R> extends AbstractTransaction<R> {
    
    private final ConnectionCallable<R> callable;

    ReturningTransaction(ConnectionCallable<R> callable) {
        this(callable, null);
    }
    
    ReturningTransaction(ConnectionCallable<R> callable, Integer isolation) {
        super(isolation);
        this.callable = callable;
    }

    public ReturningTransaction<R> isolation(int isolation){
        return new ReturningTransaction<>(callable, isolation);
    }

    public R execute(DataSource db) throws SQLException {
        if (db == null) {
            throw new IllegalArgumentException("db cannot be null");
        }
        return execute(db::getConnection);
    }

    public R execute(ConnectionSupplier db) throws SQLException {
        return super.executeInternal(db);
    }

    public R execute(Connection connection) throws SQLException {
        return super.executeInternal(connection);
    }

    @Override
    ConnectionCallable<R> callable() {
        return callable;
    }
}
