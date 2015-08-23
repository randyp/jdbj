package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ConnectionCallable;
import com.github.randyp.jdbj.lambda.ConnectionRunnable;
import com.github.randyp.jdbj.lambda.ConnectionSupplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * {@link Immutable} transaction builder. Only build operation is isolation. Example:
 * <pre>
 * {@code 
 * JDBJ.transaction(connection->System.out.println(connection.getSchema()))
 *     .execute(db);
 * }
 * </pre>
 * <p>
 * Exceptions are grouped using the {@link SQLException#setNextException(SQLException)}, so one can get all exceptions. Example:
 * <pre>
 * {@code
 * try{
 *     JDBJ.transaction(connection->System.out.println(connection.getSchema()))
 *         .execute(db);
 * }catch(SQLException e){
 *     while(e != null){
 *         e.printStackTrace();
 *         e = e.getNextException();
 *     }
 * }         
 * }
 * </pre>
 */
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
