package io.github.randyp.jdbj;

import io.github.randyp.jdbj.lambda.ConnectionCallable;
import io.github.randyp.jdbj.lambda.ConnectionSupplier;
import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.annotation.concurrent.ThreadSafe;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * {@link javax.annotation.concurrent.Immutable} transaction builder. Only build operation is isolation.
 * <p>
 * Example:
 * <pre>
 * {@code
 * String schema = JDBJ.transaction(connection->connection.getSchema())
 *     .execute(db);
 * }
 * </pre>
 * Exceptions are grouped using the {@link SQLException#setNextException(SQLException)}, so one can get all exceptions. Example:
 * <pre>
 * {@code
 * try{
 *     String schema = JDBJ.transaction(connection->connection.getSchema())
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
public class ReturningTransaction<R> extends AbstractTransaction<R> {
    
    private final ConnectionCallable<R> callable;

    ReturningTransaction(ConnectionCallable<R> callable) {
        this(callable, null);
    }
    
    ReturningTransaction(ConnectionCallable<R> callable, Integer isolation) {
        super(isolation);
        Objects.requireNonNull(callable, "callable must not be null");
        this.callable = callable;
    }

    public ReturningTransaction<R> isolation(int isolation){
        return new ReturningTransaction<>(callable, isolation);
    }

    public R execute(DataSource db) throws SQLException {
        Objects.requireNonNull(db, "db must not be null");
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
