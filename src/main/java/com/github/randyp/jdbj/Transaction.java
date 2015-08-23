package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ConnectionCallable;
import com.github.randyp.jdbj.lambda.ConnectionRunnable;
import com.github.randyp.jdbj.lambda.ConnectionSupplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Immutable
@ThreadSafe
public class Transaction extends AbstractTransaction<Void> {

    private final ConnectionRunnable runnable;

    Transaction(ConnectionRunnable runnable) {
        this(runnable, null);
    }
    
    Transaction(ConnectionRunnable runnable, Integer isolation) {
        super(isolation);
        this.runnable = runnable;
    }
    
    public Transaction isolation(int isolation){
        return new Transaction(runnable, isolation);
    }
    
    public void execute(DataSource db) throws SQLException {
        if (db == null) {
            throw new IllegalArgumentException("db cannot be null");
        }
        execute(db::getConnection);
    }
    
    public void execute(ConnectionSupplier db) throws SQLException {
        super.executeInternal(db);
    }

    public void execute(Connection connection) throws SQLException {
        super.executeInternal(connection);
    }

    @Override
    ConnectionCallable<Void> callable() {
        return connection -> {
            runnable.run(connection);
            return null;
        };
    }
}
